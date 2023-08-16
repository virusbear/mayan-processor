package com.virusbear.mayan.processor

interface MayanProcessorBuilder {
    var id: String
    fun init(block: suspend () -> Unit)
    fun accept(block: suspend Document.() -> Boolean)
    fun process(block: suspend ProcessingContext.() -> Unit)
    fun close(block: suspend () -> Unit)
}