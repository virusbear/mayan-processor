package com.virusbear.mayan.processor.scripting

import com.virusbear.mayan.processor.MayanProcessor
import com.virusbear.mayan.processor.ProcessingContext
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.apache.commons.io.monitor.FileAlterationMonitor
import org.apache.commons.io.monitor.FileAlterationObserver
import java.io.File
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

class MayanProcessorHost(
    private val scriptPath: File,
    private val libraryPath: File,
    watch: Boolean = true
) {
    companion object {
        private val logger = KotlinLogging.logger { }
    }

    private val compilationConfiguration =
        createJvmCompilationConfigurationFromTemplate<MayanProcessorScript>().with {
            set(LibraryDirectoryProperty, libraryPath)
        }
    private val scriptHost = BasicJvmScriptingHost()

    private val processors: MutableMap<String, MayanProcessor>
    private val disabledProcessors = mutableSetOf<String>()

    init {
        if(!scriptPath.exists()) {
            scriptPath.mkdirs()
        }

        processors = loadProcessors(scriptPath).toMutableMap()
        runBlocking {
            processors.values.map {
                async {
                    it.init()
                }
            }
        }

        if(watch) {
            val listener = ScriptFileAlterationListener(this, processors)
            val observer = FileAlterationObserver(scriptPath) { it.isFile && it.name.endsWith(".mayan.kts") }
            observer.addListener(listener)
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
            logger.error { "Script path is not a directory" }
            return emptyMap()
        }

        return scriptPath.walkTopDown().filter { it.name.endsWith(".mayan.kts") }.map { scriptFile ->
            loadMayanProcessor(scriptFile)
        }.filterNotNull().associateBy { it.first }.mapValues { it.value.second }
    }

    internal fun loadMayanProcessor(file: File): Pair<String, MayanProcessor>? {
        logger.info { "Loading script ${file.relativeTo(scriptPath)}" }
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