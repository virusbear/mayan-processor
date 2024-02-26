package com.virusbear.mayan.processor.scripting

import com.virusbear.mayan.processor.Document
import com.virusbear.mayan.processor.DocumentContentProvider
import com.virusbear.mayan.processor.MayanProcessor
import com.virusbear.mayan.processor.ProcessingContext

class MayanProcessorImpl(
    private val initializer: suspend () -> Unit,
    private val acceptor: suspend DocumentContentProvider.(Document) -> Boolean,
    private val processor: suspend ProcessingContext.() -> Unit,
    private val closer: suspend () -> Unit
): MayanProcessor {
    override suspend fun init() {
        initializer()
    }

    override suspend fun accept(contentProvider: DocumentContentProvider, document: Document): Boolean =
        contentProvider.acceptor(document)

    override suspend fun process(scope: ProcessingContext): Unit =
        scope.processor()

    override suspend fun close() {
        closer()
    }
}