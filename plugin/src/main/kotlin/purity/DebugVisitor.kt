package purity

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.com.intellij.psi.PsiBinaryFile
import org.jetbrains.kotlin.com.intellij.psi.PsiComment
import org.jetbrains.kotlin.com.intellij.psi.PsiDirectory
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.com.intellij.psi.PsiErrorElement
import org.jetbrains.kotlin.com.intellij.psi.PsiFile
import org.jetbrains.kotlin.com.intellij.psi.PsiPlainText
import org.jetbrains.kotlin.com.intellij.psi.PsiPlainTextFile
import org.jetbrains.kotlin.com.intellij.psi.PsiWhiteSpace
import org.jetbrains.kotlin.com.intellij.psi.templateLanguages.OuterLanguageElement
import org.jetbrains.kotlin.psi.KtAnnotatedExpression
import org.jetbrains.kotlin.psi.KtAnnotation
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtAnnotationUseSiteTarget
import org.jetbrains.kotlin.psi.KtAnonymousInitializer
import org.jetbrains.kotlin.psi.KtArrayAccessExpression
import org.jetbrains.kotlin.psi.KtBinaryExpression
import org.jetbrains.kotlin.psi.KtBinaryExpressionWithTypeRHS
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtBlockStringTemplateEntry
import org.jetbrains.kotlin.psi.KtBreakExpression
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtCallableReferenceExpression
import org.jetbrains.kotlin.psi.KtCatchClause
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassBody
import org.jetbrains.kotlin.psi.KtClassInitializer
import org.jetbrains.kotlin.psi.KtClassLiteralExpression
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtCollectionLiteralExpression
import org.jetbrains.kotlin.psi.KtConstantExpression
import org.jetbrains.kotlin.psi.KtConstructorCalleeExpression
import org.jetbrains.kotlin.psi.KtConstructorDelegationCall
import org.jetbrains.kotlin.psi.KtContinueExpression
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtDelegatedSuperTypeEntry
import org.jetbrains.kotlin.psi.KtDestructuringDeclaration
import org.jetbrains.kotlin.psi.KtDestructuringDeclarationEntry
import org.jetbrains.kotlin.psi.KtDoWhileExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtDoubleColonExpression
import org.jetbrains.kotlin.psi.KtDynamicType
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtEnumEntry
import org.jetbrains.kotlin.psi.KtEscapeStringTemplateEntry
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtExpressionWithLabel
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtFileAnnotationList
import org.jetbrains.kotlin.psi.KtFinallySection
import org.jetbrains.kotlin.psi.KtForExpression
import org.jetbrains.kotlin.psi.KtFunctionType
import org.jetbrains.kotlin.psi.KtIfExpression
import org.jetbrains.kotlin.psi.KtImportAlias
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.psi.KtImportList
import org.jetbrains.kotlin.psi.KtInitializerList
import org.jetbrains.kotlin.psi.KtIsExpression
import org.jetbrains.kotlin.psi.KtLabeledExpression
import org.jetbrains.kotlin.psi.KtLambdaExpression
import org.jetbrains.kotlin.psi.KtLiteralStringTemplateEntry
import org.jetbrains.kotlin.psi.KtLoopExpression
import org.jetbrains.kotlin.psi.KtModifierList
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtNullableType
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.KtObjectLiteralExpression
import org.jetbrains.kotlin.psi.KtPackageDirective
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtParameterList
import org.jetbrains.kotlin.psi.KtParenthesizedExpression
import org.jetbrains.kotlin.psi.KtPostfixExpression
import org.jetbrains.kotlin.psi.KtPrefixExpression
import org.jetbrains.kotlin.psi.KtPrimaryConstructor
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtPropertyAccessor
import org.jetbrains.kotlin.psi.KtPropertyDelegate
import org.jetbrains.kotlin.psi.KtQualifiedExpression
import org.jetbrains.kotlin.psi.KtReferenceExpression
import org.jetbrains.kotlin.psi.KtReturnExpression
import org.jetbrains.kotlin.psi.KtSafeQualifiedExpression
import org.jetbrains.kotlin.psi.KtScript
import org.jetbrains.kotlin.psi.KtScriptInitializer
import org.jetbrains.kotlin.psi.KtSecondaryConstructor
import org.jetbrains.kotlin.psi.KtSelfType
import org.jetbrains.kotlin.psi.KtSimpleNameExpression
import org.jetbrains.kotlin.psi.KtSimpleNameStringTemplateEntry
import org.jetbrains.kotlin.psi.KtStringTemplateEntry
import org.jetbrains.kotlin.psi.KtStringTemplateEntryWithExpression
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.KtSuperExpression
import org.jetbrains.kotlin.psi.KtSuperTypeCallEntry
import org.jetbrains.kotlin.psi.KtSuperTypeEntry
import org.jetbrains.kotlin.psi.KtSuperTypeList
import org.jetbrains.kotlin.psi.KtSuperTypeListEntry
import org.jetbrains.kotlin.psi.KtThisExpression
import org.jetbrains.kotlin.psi.KtThrowExpression
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid
import org.jetbrains.kotlin.psi.KtTryExpression
import org.jetbrains.kotlin.psi.KtTypeAlias
import org.jetbrains.kotlin.psi.KtTypeArgumentList
import org.jetbrains.kotlin.psi.KtTypeConstraint
import org.jetbrains.kotlin.psi.KtTypeConstraintList
import org.jetbrains.kotlin.psi.KtTypeParameter
import org.jetbrains.kotlin.psi.KtTypeParameterList
import org.jetbrains.kotlin.psi.KtTypeProjection
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.psi.KtUnaryExpression
import org.jetbrains.kotlin.psi.KtUserType
import org.jetbrains.kotlin.psi.KtValueArgument
import org.jetbrains.kotlin.psi.KtValueArgumentList
import org.jetbrains.kotlin.psi.KtWhenConditionInRange
import org.jetbrains.kotlin.psi.KtWhenConditionIsPattern
import org.jetbrains.kotlin.psi.KtWhenConditionWithExpression
import org.jetbrains.kotlin.psi.KtWhenEntry
import org.jetbrains.kotlin.psi.KtWhenExpression
import org.jetbrains.kotlin.psi.KtWhileExpression

class DebugVisitor(
    private val messageCollector: MessageCollector
) : KtTreeVisitorVoid() {

    @Suppress("UNUSED_PARAMETER")
    private fun log(tag: String, element: PsiElement?) {
//        messageCollector.report(
//            CompilerMessageSeverity.ERROR,
//            "$tag: $element",
//            MessageUtil.psiElementToMessageLocation(element)
//        )
    }

    private fun log(tag: String, element: KtElement?) {
        messageCollector.report(
            CompilerMessageSeverity.ERROR,
            "$tag($element)\n${element?.text}\n"
        )
    }

    override fun visitUserType(element: KtUserType) {
        log("visitUserType", element)
        super.visitUserType(element)
    }

    override fun visitFile(element: PsiFile?) {
        log("visitFile", element)
        super.visitFile(element)
    }

    override fun visitReferenceExpression(element: KtReferenceExpression) {
        log("visitReferenceExpression", element)
        super.visitReferenceExpression(element)
    }

    override fun visitCallExpression(element: KtCallExpression) {
        log("visitCallExpression", element)
        super.visitCallExpression(element)
    }

    override fun visitBlockStringTemplateEntry(element: KtBlockStringTemplateEntry) {
        log("visitBlockStringTemplateEntry", element)
        super.visitBlockStringTemplateEntry(element)
    }

    override fun visitUnaryExpression(element: KtUnaryExpression) {
        log("visitUnaryExpression", element)
        super.visitUnaryExpression(element)
    }

    override fun visitDynamicType(element: KtDynamicType) {
        log("visitDynamicType", element)
        super.visitDynamicType(element)
    }

    override fun visitDynamicType(element: KtDynamicType, data: Void?): Void {
        log("visitDynamicType", element)
        return super.visitDynamicType(element, data)
    }

    override fun visitSuperTypeCallEntry(element: KtSuperTypeCallEntry) {
        log("visitSuperTypeCallEntry", element)
        super.visitSuperTypeCallEntry(element)
    }

    override fun visitParenthesizedExpression(element: KtParenthesizedExpression) {
        log("visitParenthesizedExpression", element)
        super.visitParenthesizedExpression(element)
    }

    override fun visitFinallySection(element: KtFinallySection) {
        log("visitFinallySection", element)
        super.visitFinallySection(element)
    }

    override fun visitStringTemplateExpression(element: KtStringTemplateExpression) {
        log("visitStringTemplateExpression", element)
        super.visitStringTemplateExpression(element)
    }

    override fun visitDeclaration(element: KtDeclaration) {
        log("visitDeclaration", element)
        super.visitDeclaration(element)
    }

    override fun visitLabeledExpression(element: KtLabeledExpression) {
        log("visitLabeledExpression", element)
        super.visitLabeledExpression(element)
    }

    override fun visitEscapeStringTemplateEntry(element: KtEscapeStringTemplateEntry) {
        log("visitEscapeStringTemplateEntry", element)
        super.visitEscapeStringTemplateEntry(element)
    }

    override fun visitScript(element: KtScript) {
        log("visitScript", element)
        super.visitScript(element)
    }

    override fun visitTypeConstraintList(element: KtTypeConstraintList) {
        log("visitTypeConstraintList", element)
        super.visitTypeConstraintList(element)
    }

    override fun visitArgument(element: KtValueArgument) {
        log("visitArgument", element)
        super.visitArgument(element)
    }

    override fun visitDotQualifiedExpression(element: KtDotQualifiedExpression) {
        log("visitDotQualifiedExpression", element)
        super.visitDotQualifiedExpression(element)
    }

    override fun visitConstructorDelegationCall(element: KtConstructorDelegationCall) {
        log("visitConstructorDelegationCall", element)
        super.visitConstructorDelegationCall(element)
    }

    override fun visitImportAlias(element: KtImportAlias) {
        log("visitImportAlias", element)
        super.visitImportAlias(element)
    }

    override fun visitImportAlias(element: KtImportAlias, data: Void?): Void {
        log("visitImportAlias", element)
        return super.visitImportAlias(element, data)
    }

    override fun visitFileAnnotationList(element: KtFileAnnotationList, data: Void?): Void {
        log("visitFileAnnotationList", element)
        return super.visitFileAnnotationList(element, data)
    }

    override fun visitWhenConditionIsPattern(element: KtWhenConditionIsPattern) {
        log("visitWhenConditionIsPattern", element)
        super.visitWhenConditionIsPattern(element)
    }

    override fun visitLoopExpression(element: KtLoopExpression) {
        log("visitLoopExpression", element)
        super.visitLoopExpression(element)
    }

    override fun visitPostfixExpression(element: KtPostfixExpression) {
        log("visitPostfixExpression", element)
        super.visitPostfixExpression(element)
    }

    override fun visitClassOrObject(element: KtClassOrObject) {
        log("visitClassOrObject", element)
        super.visitClassOrObject(element)
    }

    override fun visitTypeArgumentList(element: KtTypeArgumentList) {
        log("visitTypeArgumentList", element)
        super.visitTypeArgumentList(element)
    }

    override fun visitClassLiteralExpression(element: KtClassLiteralExpression) {
        log("visitClassLiteralExpression", element)
        super.visitClassLiteralExpression(element)
    }

    override fun visitModifierList(element: KtModifierList) {
        log("visitModifierList", element)
        super.visitModifierList(element)
    }

    override fun visitTypeParameterList(element: KtTypeParameterList) {
        log("visitTypeParameterList", element)
        super.visitTypeParameterList(element)
    }

    override fun visitImportList(element: KtImportList) {
        log("visitImportList", element)
        super.visitImportList(element)
    }

    override fun visitAnnotatedExpression(element: KtAnnotatedExpression) {
        log("visitAnnotatedExpression", element)
        super.visitAnnotatedExpression(element)
    }

    override fun visitDestructuringDeclaration(element: KtDestructuringDeclaration) {
        log("visitDestructuringDeclaration", element)
        super.visitDestructuringDeclaration(element)
    }

    override fun visitPlainTextFile(element: PsiPlainTextFile?) {
        log("visitPlainTextFile", element)
        super.visitPlainTextFile(element)
    }

    override fun visitTryExpression(element: KtTryExpression) {
        log("visitTryExpression", element)
        super.visitTryExpression(element)
    }

    override fun visitScriptInitializer(element: KtScriptInitializer) {
        log("visitScriptInitializer", element)
        super.visitScriptInitializer(element)
    }

    override fun visitScriptInitializer(element: KtScriptInitializer, data: Void?): Void {
        log("visitScriptInitializer", element)
        return super.visitScriptInitializer(element, data)
    }

    override fun visitEnumEntry(element: KtEnumEntry) {
        log("visitEnumEntry", element)
        super.visitEnumEntry(element)
    }

    override fun visitNamedDeclaration(element: KtNamedDeclaration) {
        log("visitNamedDeclaration", element)
        super.visitNamedDeclaration(element)
    }

    override fun visitSuperTypeListEntry(element: KtSuperTypeListEntry) {
        log("visitSuperTypeListEntry", element)
        super.visitSuperTypeListEntry(element)
    }

    override fun visitCollectionLiteralExpression(element: KtCollectionLiteralExpression) {
        log("visitCollectionLiteralExpression", element)
        super.visitCollectionLiteralExpression(element)
    }

    override fun visitCollectionLiteralExpression(element: KtCollectionLiteralExpression, data: Void?): Void {
        log("visitCollectionLiteralExpression", element)
        return super.visitCollectionLiteralExpression(element, data)
    }

    override fun visitClassInitializer(element: KtClassInitializer) {
        log("visitClassInitializer", element)
        super.visitClassInitializer(element)
    }

    override fun visitClassInitializer(element: KtClassInitializer, data: Void?): Void {
        log("visitClassInitializer", element)
        return super.visitClassInitializer(element, data)
    }

    override fun visitSuperTypeList(element: KtSuperTypeList) {
        log("visitSuperTypeList", element)
        super.visitSuperTypeList(element)
    }

    override fun visitThrowExpression(element: KtThrowExpression) {
        log("visitThrowExpression", element)
        super.visitThrowExpression(element)
    }

    override fun visitAnnotationUseSiteTarget(element: KtAnnotationUseSiteTarget, data: Void?): Void {
        log("visitAnnotationUseSiteTarget", element)
        return super.visitAnnotationUseSiteTarget(element, data)
    }

    override fun visitReturnExpression(element: KtReturnExpression) {
        log("visitReturnExpression", element)
        super.visitReturnExpression(element)
    }

    override fun visitArrayAccessExpression(element: KtArrayAccessExpression) {
        log("visitArrayAccessExpression", element)
        super.visitArrayAccessExpression(element)
    }

    override fun visitThisExpression(element: KtThisExpression) {
        log("visitThisExpression", element)
        super.visitThisExpression(element)
    }

    override fun visitForExpression(element: KtForExpression) {
        log("visitForExpression", element)
        super.visitForExpression(element)
    }

    override fun visitAnnotation(element: KtAnnotation) {
        log("visitAnnotation", element)
        super.visitAnnotation(element)
    }

    override fun visitSimpleNameStringTemplateEntry(element: KtSimpleNameStringTemplateEntry) {
        log("visitSimpleNameStringTemplateEntry", element)
        super.visitSimpleNameStringTemplateEntry(element)
    }

    override fun visitWhenExpression(element: KtWhenExpression) {
        log("visitWhenExpression", element)
        super.visitWhenExpression(element)
    }

    override fun visitBlockExpression(element: KtBlockExpression) {
        log("visitBlockExpression", element)
        super.visitBlockExpression(element)
    }

    override fun visitTypeProjection(element: KtTypeProjection) {
        log("visitTypeProjection", element)
        super.visitTypeProjection(element)
    }

    override fun visitLambdaExpression(element: KtLambdaExpression) {
        log("visitLambdaExpression", element)
        super.visitLambdaExpression(element)
    }

    override fun visitKtFile(element: KtFile) {
        log("visitKtFile", element)
        super.visitKtFile(element)
    }

    override fun visitWhiteSpace(element: PsiWhiteSpace?) {
        log("visitWhiteSpace", element)
        super.visitWhiteSpace(element)
    }

    override fun visitOuterLanguageElement(element: OuterLanguageElement?) {
        log("visitOuterLanguageElement", element)
        super.visitOuterLanguageElement(element)
    }

    override fun visitDoubleColonExpression(element: KtDoubleColonExpression) {
        log("visitDoubleColonExpression", element)
        super.visitDoubleColonExpression(element)
    }

    override fun visitConstantExpression(element: KtConstantExpression) {
        log("visitConstantExpression", element)
        super.visitConstantExpression(element)
    }

    override fun visitNullableType(element: KtNullableType) {
        log("visitNullableType", element)
        super.visitNullableType(element)
    }

    override fun visitWhenConditionWithExpression(element: KtWhenConditionWithExpression) {
        log("visitWhenConditionWithExpression", element)
        super.visitWhenConditionWithExpression(element)
    }

    override fun visitIfExpression(element: KtIfExpression) {
        log("visitIfExpression", element)
        super.visitIfExpression(element)
    }

    override fun visitExpressionWithLabel(element: KtExpressionWithLabel) {
        log("visitExpressionWithLabel", element)
        super.visitExpressionWithLabel(element)
    }

    override fun visitDirectory(element: PsiDirectory?) {
        log("visitDirectory", element)
        super.visitDirectory(element)
    }

    override fun visitTypeAlias(element: KtTypeAlias) {
        log("visitTypeAlias", element)
        super.visitTypeAlias(element)
    }

    override fun visitPropertyDelegate(element: KtPropertyDelegate) {
        log("visitPropertyDelegate", element)
        super.visitPropertyDelegate(element)
    }

    override fun visitDelegatedSuperTypeEntry(element: KtDelegatedSuperTypeEntry) {
        log("visitDelegatedSuperTypeEntry", element)
        super.visitDelegatedSuperTypeEntry(element)
    }

    override fun visitDestructuringDeclarationEntry(element: KtDestructuringDeclarationEntry) {
        log("visitDestructuringDeclarationEntry", element)
        super.visitDestructuringDeclarationEntry(element)
    }

    override fun visitBreakExpression(element: KtBreakExpression) {
        log("visitBreakExpression", element)
        super.visitBreakExpression(element)
    }

    override fun visitPrefixExpression(element: KtPrefixExpression) {
        log("visitPrefixExpression", element)
        super.visitPrefixExpression(element)
    }

    override fun visitImportDirective(element: KtImportDirective) {
        log("visitImportDirective", element)
        super.visitImportDirective(element)
    }

    override fun visitSimpleNameExpression(element: KtSimpleNameExpression) {
        log("visitSimpleNameExpression", element)
        super.visitSimpleNameExpression(element)
    }

    override fun visitElement(element: PsiElement) {
        log("visitElement", element)
        super.visitElement(element)
    }

    override fun visitParameterList(element: KtParameterList) {
        log("visitParameterList", element)
        super.visitParameterList(element)
    }

    override fun visitSecondaryConstructor(element: KtSecondaryConstructor) {
        log("visitSecondaryConstructor", element)
        super.visitSecondaryConstructor(element)
    }

    override fun visitConstructorCalleeExpression(element: KtConstructorCalleeExpression) {
        log("visitConstructorCalleeExpression", element)
        super.visitConstructorCalleeExpression(element)
    }

    override fun visitObjectLiteralExpression(element: KtObjectLiteralExpression) {
        log("visitObjectLiteralExpression", element)
        super.visitObjectLiteralExpression(element)
    }

    override fun visitObjectDeclaration(element: KtObjectDeclaration) {
        log("visitObjectDeclaration", element)
        super.visitObjectDeclaration(element)
    }

    override fun visitQualifiedExpression(element: KtQualifiedExpression) {
        log("visitQualifiedExpression", element)
        super.visitQualifiedExpression(element)
    }

    override fun visitDoWhileExpression(element: KtDoWhileExpression) {
        log("visitDoWhileExpression", element)
        super.visitDoWhileExpression(element)
    }

    override fun visitAnonymousInitializer(element: KtAnonymousInitializer) {
        log("visitAnonymousInitializer", element)
        super.visitAnonymousInitializer(element)
    }

    override fun visitLiteralStringTemplateEntry(element: KtLiteralStringTemplateEntry) {
        log("visitLiteralStringTemplateEntry", element)
        super.visitLiteralStringTemplateEntry(element)
    }

    override fun visitIsExpression(element: KtIsExpression) {
        log("visitIsExpression", element)
        super.visitIsExpression(element)
    }

    override fun visitBinaryExpression(element: KtBinaryExpression) {
        log("visitBinaryExpression", element)
        super.visitBinaryExpression(element)
    }

    override fun visitWhenEntry(element: KtWhenEntry) {
        log("visitWhenEntry", element)
        super.visitWhenEntry(element)
    }

    override fun visitCallableReferenceExpression(element: KtCallableReferenceExpression) {
        log("visitCallableReferenceExpression", element)
        super.visitCallableReferenceExpression(element)
    }

    override fun visitContinueExpression(element: KtContinueExpression) {
        log("visitContinueExpression", element)
        super.visitContinueExpression(element)
    }

    override fun visitCatchSection(element: KtCatchClause) {
        log("visitCatchSection", element)
        super.visitCatchSection(element)
    }

    override fun visitStringTemplateEntryWithExpression(element: KtStringTemplateEntryWithExpression) {
        log("visitStringTemplateEntryWithExpression", element)
        super.visitStringTemplateEntryWithExpression(element)
    }

    override fun visitWhenConditionInRange(element: KtWhenConditionInRange) {
        log("visitWhenConditionInRange", element)
        super.visitWhenConditionInRange(element)
    }

    override fun visitParameter(element: KtParameter) {
        log("visitParameter", element)
        super.visitParameter(element)
    }

    override fun visitTypeParameter(element: KtTypeParameter) {
        log("visitTypeParameter", element)
        super.visitTypeParameter(element)
    }

    override fun visitPrimaryConstructor(element: KtPrimaryConstructor) {
        log("visitPrimaryConstructor", element)
        super.visitPrimaryConstructor(element)
    }

    override fun visitWhileExpression(element: KtWhileExpression) {
        log("visitWhileExpression", element)
        super.visitWhileExpression(element)
    }

    override fun visitBinaryFile(element: PsiBinaryFile?) {
        log("visitBinaryFile", element)
        super.visitBinaryFile(element)
    }

    override fun visitStringTemplateEntry(element: KtStringTemplateEntry) {
        log("visitStringTemplateEntry", element)
        super.visitStringTemplateEntry(element)
    }

    override fun visitClass(element: KtClass) {
        log("visitClass", element)
        super.visitClass(element)
    }

    override fun visitSuperExpression(element: KtSuperExpression) {
        log("visitSuperExpression", element)
        super.visitSuperExpression(element)
    }

    override fun visitSafeQualifiedExpression(element: KtSafeQualifiedExpression) {
        log("visitSafeQualifiedExpression", element)
        super.visitSafeQualifiedExpression(element)
    }

    override fun visitInitializerList(element: KtInitializerList) {
        log("visitInitializerList", element)
        super.visitInitializerList(element)
    }

    override fun visitBinaryWithTypeRHSExpression(element: KtBinaryExpressionWithTypeRHS) {
        log("visitBinaryWithTypeRHSExpression", element)
        super.visitBinaryWithTypeRHSExpression(element)
    }

    override fun visitFunctionType(element: KtFunctionType) {
        log("visitFunctionType", element)
        super.visitFunctionType(element)
    }

    override fun visitNamedFunction(element: KtNamedFunction) {
        log("visitNamedFunction", element)
        super.visitNamedFunction(element)
    }

    override fun visitExpression(element: KtExpression) {
        log("visitExpression", element)
        super.visitExpression(element)
    }

    override fun visitSelfType(element: KtSelfType) {
        log("visitSelfType", element)
        super.visitSelfType(element)
    }

    override fun visitPackageDirective(element: KtPackageDirective) {
        log("visitPackageDirective", element)
        super.visitPackageDirective(element)
    }

    override fun visitTypeReference(element: KtTypeReference) {
        log("visitTypeReference", element)
        super.visitTypeReference(element)
    }

    override fun visitClassBody(element: KtClassBody) {
        log("visitClassBody", element)
        super.visitClassBody(element)
    }

    override fun visitKtElement(element: KtElement) {
        log("visitKtElement", element)
        super.visitKtElement(element)
    }

    override fun visitSuperTypeEntry(element: KtSuperTypeEntry) {
        log("visitSuperTypeEntry", element)
        super.visitSuperTypeEntry(element)
    }

    override fun visitAnnotationEntry(element: KtAnnotationEntry) {
        log("visitAnnotationEntry", element)
        super.visitAnnotationEntry(element)
    }

    override fun visitPropertyAccessor(element: KtPropertyAccessor) {
        log("visitPropertyAccessor", element)
        super.visitPropertyAccessor(element)
    }

    override fun visitTypeConstraint(element: KtTypeConstraint) {
        log("visitTypeConstraint", element)
        super.visitTypeConstraint(element)
    }

    override fun visitProperty(element: KtProperty) {
        log("visitProperty", element)
        super.visitProperty(element)
    }

    override fun visitErrorElement(element: PsiErrorElement?) {
        log("visitErrorElement", element)
        super.visitErrorElement(element)
    }

    override fun visitComment(element: PsiComment?) {
        log("visitComment", element)
        super.visitComment(element)
    }

    override fun visitValueArgumentList(element: KtValueArgumentList) {
        log("visitValueArgumentList", element)
        super.visitValueArgumentList(element)
    }

    override fun visitPlainText(element: PsiPlainText?) {
        log("visitPlainText", element)
        super.visitPlainText(element)
    }
}
