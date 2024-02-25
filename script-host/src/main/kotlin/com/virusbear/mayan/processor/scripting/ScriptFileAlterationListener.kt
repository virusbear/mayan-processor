package com.virusbear.mayan.processor.scripting

import com.virusbear.mayan.processor.MayanProcessor
import com.virusbear.mayan.processor.scripting.MayanProcessorHost
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor
import java.io.File

internal class ScriptFileAlterationListener(
    private val host: MayanProcessorHost,
    private val processors: MutableMap<String, MayanProcessor>
): FileAlterationListenerAdaptor() {
    override fun onFileCreate(file: File) {
        reloadProcessor(file)
    }

    override fun onFileChange(file: File) {
        reloadProcessor(file)
    }

    override fun onFileDelete(file: File) {
        val (id, _) = host.loadMayanProcessor(file) ?: return
        closeProcessor(id)
        processors -= id
    }

    private fun reloadProcessor(file: File) {
        val (id, processor) = host.loadMayanProcessor(file) ?: return
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
}