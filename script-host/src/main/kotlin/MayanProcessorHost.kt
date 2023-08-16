import com.virusbear.mayan.processor.MayanProcessor
import com.virusbear.mayan.processor.ProcessingContext
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor
import org.apache.commons.io.monitor.FileAlterationMonitor
import org.apache.commons.io.monitor.FileAlterationObserver
import java.io.File
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

class MayanProcessorHost(
    private val scriptPath: File,
    watch: Boolean = true
) {
    companion object {
        private val logger = KotlinLogging.logger { }
    }

    private val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<MayanProcessorScript>()
    private val scriptHost = BasicJvmScriptingHost()

    private val processors = loadProcessors(scriptPath).toMutableMap()
    private val disabledProcessors = mutableSetOf<String>()

    init {
        runBlocking {
            processors.values.map {
                async {
                    it.init()
                }
            }
        }

        if(watch) {
            val observer = FileAlterationObserver(scriptPath) { it.isFile && it.name.endsWith(".mayan.kts") }
            observer.addListener(object: FileAlterationListenerAdaptor() {
                override fun onFileCreate(file: File) {
                    reloadProcessor(file)
                }

                override fun onFileChange(file: File) {
                    reloadProcessor(file)
                }

                override fun onFileDelete(file: File) {
                    val (id, _) = loadMayanProcessor(file) ?: return
                    closeProcessor(id)
                }

                private fun reloadProcessor(file: File) {
                    val (id, processor) = loadMayanProcessor(file) ?: return
                    closeProcessor(id)
                    processors[id] = processor
                    runBlocking {
                        processor.init()
                    }
                }

                private fun closeProcessor(id: String) {
                    runBlocking {
                        processors[id]?.close()
                    }
                }
            })
            FileAlterationMonitor(3L, observer).start()
        }
    }

    fun disable(processorId: String) {
        disabledProcessors += processorId
    }

    fun enable(processorId: String) {
        disabledProcessors -= processorId
    }

    suspend fun process(context: ProcessingContext) {
        processors.filterKeys { it !in disabledProcessors }.toList().firstOrNull { (_, processor) ->
            processor.accept(context.document)
        }?.second?.process(context)
    }

    private fun loadProcessors(scriptPath: File): Map<String, MayanProcessor> {
        if(!scriptPath.isDirectory) {
            logger.error { "Scriptpath is not a directory" }
            return emptyMap()
        }

        return scriptPath.walkTopDown().filter { it.name.endsWith(".mayan.kts") }.map { scriptFile ->
            loadMayanProcessor(scriptFile)
        }.filterNotNull().associateBy { it.first }.mapValues { it.value.second }
    }

    private fun loadMayanProcessor(file: File): Pair<String, MayanProcessor>? {
        logger.info { "loading script ${file.relativeTo(scriptPath)}" }
        val builder = MayanProcessorBuilderImpl(file.name.removeSuffix(".mayan.kts"))

        return scriptHost.eval(file.toScriptSource(), compilationConfiguration, ScriptEvaluationConfiguration{
            implicitReceivers(builder)
        }).onSuccess {
            ResultWithDiagnostics.Success(builder.build())
        }.onFailure {
            logger.error { "Failed to load processor script ${file.relativeTo(scriptPath)}\n${it.reports.joinToString("\n")}" }
        }.valueOrNull()
    }
}