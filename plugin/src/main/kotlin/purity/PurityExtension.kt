package purity

import org.jetbrains.kotlin.analyzer.AnalysisResult
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension

class PurityExtension(
    private val messageCollector: MessageCollector
) : AnalysisHandlerExtension {

    override fun analysisCompleted(
        project: Project,
        module: ModuleDescriptor,
        bindingTrace: BindingTrace,
        files: Collection<KtFile>
    ): AnalysisResult? {
        @Suppress("UNUSED_VARIABLE")
        val debugVisitor = DebugVisitor(messageCollector)
        val visitor = PurityValidateVisitor(bindingTrace.bindingContext, messageCollector)
        files.forEach {
            // it.acceptChildren(debugVisitor)
            it.acceptChildren(visitor)
        }
        return null
    }

}
