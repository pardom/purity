package purity

import org.jetbrains.kotlin.backend.common.push
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.common.messages.MessageUtil
import org.jetbrains.kotlin.descriptors.CallableDescriptor
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.bindingContextUtil.isUsedAsExpression

class PurityValidateVisitor(
    private val bindingContext: BindingContext,
    private val messageCollector: MessageCollector
) : KtTreeVisitorVoid() {

    private val functionProperties = mutableMapOf<CallableDescriptor, MutableList<String>>()

    override fun visitKtElement(element: KtElement) {
        var skipChildren = false
        when (element) {
            is KtFunction -> {
                element.functionDescriptor(bindingContext) {
                    if (declaredPure()) {
                        element.functionDescriptor(bindingContext) {
                            functionProperties.putIfAbsent(this, mutableListOf())
                        }
                    } else {
                        skipChildren = true
                    }
                }
            }
            is KtParameter -> {
                element.ownerFunction?.functionDescriptor(bindingContext) {
                    if (declaredPure()) {
                        // val type = bindingContext.get(BindingContext.TYPE, element.typeReference)
                        // error(element, "Mutable parameter '$name' passed to a pure context.")
                    }
                }
            }
            is KtCallExpression -> {
                element.functionDescriptor(bindingContext) {
                    if (!declaredPure()) {
                        error(element, "Impure function '$name' called from a pure context.")
                    }
                }
            }
            is KtProperty -> {
                element.enclosingFunctionDescriptor(bindingContext) {
                    if (declaredPure()) {
                        functionProperties[this]?.push(element.name!!)
                    }
                }
            }
            is KtNameReferenceExpression -> {
                element.enclosingFunctionDescriptor(bindingContext) {
                    val hasSideEffect = declaredPure() &&
                            element.isUsedAsExpression(bindingContext) &&
                            element.getReferencedName() !in functionProperties[this].orEmpty()

                    if (hasSideEffect) {
                        error(element, "Non-local property called from a pure context.")
                    }
                }
            }
        }
        if (!skipChildren) {
            element.acceptChildren(this)
        }
    }

    private fun error(element: KtElement, msg: String) {
        messageCollector.report(
            CompilerMessageSeverity.ERROR,
            msg,
            MessageUtil.psiElementToMessageLocation(element)
        )
    }

}
