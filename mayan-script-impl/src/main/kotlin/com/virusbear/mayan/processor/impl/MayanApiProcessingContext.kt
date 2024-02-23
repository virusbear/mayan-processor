package com.virusbear.mayan.processor.impl

import com.virusbear.mayan.processor.Document
import com.virusbear.mayan.processor.ProcessingContext
import com.virusbear.mayan.client.model.Document as ApiDocument

class MayanApiProcessingContext(
    private val doc: ApiDocument
): ProcessingContext {
    override val document: Document
        get() = MayanDocument(doc)

    override suspend fun regex(pattern: String, group: Int): String =
        pattern.toRegex().find(document.content())?.groups?.get(group)?.value ?: ""

    override suspend fun regex(pattern: String, group: String): String =
        pattern.toRegex().find(document.content())?.groups?.get(group)?.value ?: ""

    override suspend fun tag(tag: String) {
        TODO("Not yet supported")
    }

    override suspend fun metadata(type: String, value: String) {
        TODO("Not yet supported")
    }

    override suspend fun documentType(type: String) {
        TODO("Not yet supported")
    }

    override suspend fun cabinet(index: String) {
        TODO("Not yet supported")
    }
}