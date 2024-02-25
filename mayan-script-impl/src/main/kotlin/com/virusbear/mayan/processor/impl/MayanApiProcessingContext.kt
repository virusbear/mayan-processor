package com.virusbear.mayan.processor.impl

import com.virusbear.mayan.client.MayanApi
import com.virusbear.mayan.processor.Document
import com.virusbear.mayan.processor.ProcessingContext
import kotlinx.coroutines.flow.firstOrNull
import com.virusbear.mayan.client.Document as ApiDocument

class MayanApiProcessingContext(
    private val api: MayanApi,
    private val doc: ApiDocument
): ProcessingContext {
    override val document: Document
        get() = MayanDocument(doc)

    override suspend fun regex(pattern: String, group: Int): String =
        pattern.toRegex().find(document.content())?.groups?.get(group)?.value ?: ""

    override suspend fun regex(pattern: String, group: String): String =
        pattern.toRegex().find(document.content())?.groups?.get(group)?.value ?: ""

    override suspend fun documentType(label: String) {
        val id = api.documentTypes.find(label = label)?.id ?: return
        doc.changeType(id)
    }

    override val Document.tags: ProcessingContext.DocumentTags by lazy {
        object: ProcessingContext.DocumentTags {
            override suspend fun plusAssign(label: String) {
                val id = api.tags.find(label = label)?.id ?: return
                doc.attachTag(id)
            }

            override suspend fun minusAssign(label: String) {
                val id = api.tags.find(label = label)?.id ?: return
                doc.removeTag(id)
            }
        }
    }

    override val Document.metadata: ProcessingContext.DocumentMetadata by lazy {
        object: ProcessingContext.DocumentMetadata {
            override suspend fun plusAssign(metadata: Pair<String, String?>) {
                val found = doc.findMetadata(metadata.first)
                if(found == null) {
                    val id = api.metadataTypes.find(metadata.first)?.id!!
                    doc.addMetadata(id, metadata.second)
                } else {
                    found.setValue(metadata.second)
                }
            }

            override suspend fun minusAssign(label: String) {
                doc.findMetadata(label)?.delete()
            }
        }
    }

    override val Document.cabinet: ProcessingContext.DocumentCabinets by lazy {
        object: ProcessingContext.DocumentCabinets {
            override suspend fun plusAssign(label: String) {
                api.cabinets.find(fullPath = label)?.addDocument(doc.id)
            }

            override suspend fun minusAssign(label: String) {
                api.cabinets.find(fullPath = label)?.removeDocument(doc.id)
            }
        }
    }
}