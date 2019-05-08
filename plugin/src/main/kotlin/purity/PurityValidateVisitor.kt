package purity

import org.jetbrains.kotlin.backend.common.push
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.common.messages.MessageUtil
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.KtModifierList
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.resolve.BindingContext

class PurityValidateVisitor(
    private val bindingContext: BindingContext,
    private val messageCollector: MessageCollector
) : PsiElementVisitor() {

    private val classProperties = mutableMapOf<KtElement, MutableList<String>>()
    private val functionProperties = mutableMapOf<KtElement, MutableList<String>>()

    override fun visitElement(element: PsiElement) {
        var visitChildren = true
        when (element) {
            is KtModifierList -> {
                visitChildren = false
            }
            is KtFunction -> {
                if (element.declaredPure(bindingContext)) {
                    functionProperties.putIfAbsent(element, mutableListOf())
                } else {
                    visitChildren = false
                }
            }
            is KtParameter -> {
                element.withAncestor<KtClassOrObject> {
                    if (declaredImmutable(bindingContext)) {
                        error(element, "Immutable class property")
                    }
                    visitChildren = false
                }
                element.withAncestor<KtFunction> {
                    if (declaredPure(bindingContext)) {
                        // val type = bindingContext.get(BindingContext.TYPE, element.typeReference)
                        // error(element, "Mutable parameter '$name' passed to a pure context.")
                    }
                    visitChildren = false
                }
            }
            is KtCallExpression -> {
                if (!element.declaredPure(bindingContext)) {
                    error(element, "Impure function called from a pure function.")
                    visitChildren = false
                }
            }
            is KtNamedDeclaration -> {
                element.withAncestor<KtFunction> {
                    if (declaredPure(bindingContext)) {
                        functionProperties[this]?.push(element.name!!)
                    }
                    visitChildren = false
                }
            }
            is KtNameReferenceExpression -> {
                element.withAncestor<KtFunction> {
                    val hasSideEffect = declaredPure(bindingContext) &&
                            element.getReferencedName() !in functionProperties[this].orEmpty()

                    if (hasSideEffect) {
                        error(element, "Non-local property referenced from a pure function.")
                    }
                }
            }
        }
        if (visitChildren) {
            element.acceptChildren(this)
        }
    }

    private fun error(element: KtElement, msg: String) {
        messageCollector.report(CompilerMessageSeverity.ERROR, msg, MessageUtil.psiElementToMessageLocation(element))
    }

    fun report() {

        messageCollector.report(
            CompilerMessageSeverity.ERROR,
            """

                classProperties: $classProperties,
                functionProperties: $functionProperties
            """.trimIndent()
        )
    }

}
