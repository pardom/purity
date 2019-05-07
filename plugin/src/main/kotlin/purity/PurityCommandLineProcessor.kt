package purity

import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor

class PurityCommandLineProcessor : CommandLineProcessor {

    override val pluginId: String = PLUGIN_ID
    override val pluginOptions: Collection<AbstractCliOption> = emptyList()

    companion object {
        const val PLUGIN_ID = "purity"
    }

}
