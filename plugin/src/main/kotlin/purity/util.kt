package purity

import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.descriptors.annotations.Annotated
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtAnnotated
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getCall
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall

private const val PURE_FQCN = "purity.Pure"
private const val IMMUTABLE_FQCN = "purity.Immutable"

fun KtFunction.declaredPure(bindingContext: BindingContext): Boolean {
    return bindingContext.get(BindingContext.FUNCTION, this)
        ?.hasAnnotation(PURE_FQCN)
        ?: false
}

fun KtCallExpression.declaredPure(bindingContext: BindingContext): Boolean {
    return getCall(bindingContext)
        .getResolvedCall(bindingContext)
        ?.resultingDescriptor
        ?.hasAnnotation(PURE_FQCN)
        ?: false
}

fun KtClassOrObject.declaredImmutable(bindingContext: BindingContext): Boolean {
    return hasAnnotation(bindingContext, IMMUTABLE_FQCN)
}

fun KtAnnotated.hasAnnotation(bindingContext: BindingContext, fqName: String): Boolean {
    return annotationEntries.any { annotationEntry ->
        val descriptor = bindingContext.get(BindingContext.ANNOTATION, annotationEntry)
        descriptor?.fqName == FqName(fqName)
    }
}

fun Annotated.hasAnnotation(fqName: String): Boolean {
    return annotations.hasAnnotation(FqName(fqName))
}

inline fun <reified T : KtElement> KtElement.withAncestor(block: T.() -> Unit): T? {
    val ancestor = findAncestor<T>()
    ancestor?.block()
    return ancestor

}

inline fun <reified T : KtElement> KtElement.findAncestor(): T? {
    if (this is T) return this
    var element = parent
    while (element != null) {
        if (element is T) return element
        element = element.parent
    }
    return null
}

fun PsiElement.toTreeString(depth: Int = 0): String {
    return "\n" + "  ".repeat(depth) +
            javaClass.simpleName +
            children.joinToString("") { it.toTreeString(depth + 1) }
}
