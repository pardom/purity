package purity

import org.jetbrains.kotlin.descriptors.CallableDescriptor
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.annotations.Annotated
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtCallElement
import org.jetbrains.kotlin.psi.KtDeclarationWithBody
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.BindingContextUtils
import org.jetbrains.kotlin.resolve.calls.callUtil.getCall
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall

fun KtDeclarationWithBody.functionDescriptor(
    bindingContext: BindingContext,
    f: FunctionDescriptor.() -> Unit = {}
): CallableDescriptor? {
    val descriptor = bindingContext.get(BindingContext.FUNCTION, this)
    if (descriptor != null) f(descriptor)
    return descriptor
}

fun KtCallElement.functionDescriptor(
    bindingContext: BindingContext,
    f: CallableDescriptor.() -> Unit = {}
): CallableDescriptor? {
    val descriptor = getCall(bindingContext).getResolvedCall(bindingContext)?.resultingDescriptor
    if (descriptor != null) f(descriptor)
    return descriptor
}

fun KtElement.enclosingFunctionDescriptor(
    bindingContext: BindingContext,
    f: FunctionDescriptor.() -> Unit = {}
): FunctionDescriptor? {
    val descriptor = BindingContextUtils.getEnclosingFunctionDescriptor(bindingContext, this)
    if (descriptor != null) f(descriptor)
    return descriptor
}

fun Annotated.declaredPure(): Boolean {
    return hasAnnotation("purity.Pure")
}

fun Annotated.declaredImmutable(): Boolean {
    return hasAnnotation("purity.Immutable")
}

private fun Annotated.hasAnnotation(fqName: String): Boolean {
    return annotations.hasAnnotation(FqName(fqName))
}
