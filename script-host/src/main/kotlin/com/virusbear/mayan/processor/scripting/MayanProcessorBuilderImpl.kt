package com.virusbear.mayan.processor.scripting

import com.virusbear.mayan.processor.Document
import com.virusbear.mayan.processor.MayanProcessor
import com.virusbear.mayan.processor.MayanProcessorBuilder
import com.virusbear.mayan.processor.ProcessingContext

class MayanProcessorBuilderImpl(override var id: String) : MayanProcessorBuilder {
    private var initializer: (suspend () -> Unit) = { }
    private var acceptor: (suspend (Document) -> Boolean) = { false }
    private var processor: (suspend ProcessingContext.() -> Unit) = { }
    private var closer: (suspend () -> Unit) = { }

    override fun init(block: suspend () -> Unit) {
        initializer = block
    }

    override fun accept(block: suspend (Document) -> Boolean) {
        acceptor = block
    }

    override fun process(block: suspend ProcessingContext.() -> Unit) {
        processor = block
    }

    override fun close(block: suspend () -> Unit) {
        closer = block
    }

    fun build(): Pair<String, MayanProcessor> =
        id to MayanProcessorImpl(initializer, acceptor, processor, closer)
}