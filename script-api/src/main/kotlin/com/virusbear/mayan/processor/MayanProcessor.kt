package com.virusbear.mayan.processor

interface MayanProcessor {
    suspend fun init()
    suspend fun accept(contentProvider: DocumentContentProvider, document: Document): Boolean
    suspend fun process(scope: ProcessingContext)
    suspend fun close()
}