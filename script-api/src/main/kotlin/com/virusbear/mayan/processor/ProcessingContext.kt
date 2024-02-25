package com.virusbear.mayan.processor

interface ProcessingContext {
    val document: Document

    suspend fun regex(pattern: String, group: Int = 1): String
    suspend fun regex(pattern: String, group: String): String

    suspend fun documentType(label: String)

    val Document.tags: DocumentTags
    val Document.metadata: DocumentMetadata
    val Document.cabinet: DocumentCabinets

    interface DocumentTags {
        suspend operator fun plusAssign(label: String)
        suspend operator fun minusAssign(label: String)
    }
    interface DocumentMetadata {
        suspend operator fun plusAssign(metadata: Pair<String, String?>)
        suspend operator fun minusAssign(label: String)
    }
    interface DocumentCabinets {
        suspend operator fun plusAssign(label: String)
        suspend operator fun minusAssign(label: String)
    }
}