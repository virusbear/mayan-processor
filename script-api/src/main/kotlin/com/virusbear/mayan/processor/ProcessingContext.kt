package com.virusbear.mayan.processor

interface ProcessingContext: DocumentContentProvider {
    val document: Document

    suspend fun type(label: String)

    val Document.tags: DocumentTags
    val Document.metadata: DocumentMetadata
    val Document.cabinets: DocumentCabinets

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