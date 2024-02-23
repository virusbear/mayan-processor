package com.virusbear.mayan.processor.impl

import com.virusbear.mayan.client.MayanClient
import com.virusbear.mayan.processor.Document
import com.virusbear.mayan.processor.ProcessingContext
import com.virusbear.mayan.client.model.Document as ApiDocument

class MayanApiProcessingContext(
    private val client: MayanClient,
    private val doc: ApiDocument
): ProcessingContext {
    override val document: Document
        get() = MayanDocument(doc)

    override suspend fun regex(pattern: String, group: Int): String =
        pattern.toRegex().find(document.content())?.groups?.get(group)?.value ?: ""

    override suspend fun regex(pattern: String, group: String): String =
        pattern.toRegex().find(document.content())?.groups?.get(group)?.value ?: ""

    override suspend fun documentType(label: String) {
        val id = client.documentTypes.listDocumentTypes().firstOrNull { it.label == label }?.id ?: return
        doc.changeType(id)
    }

    override val Document.tags: ProcessingContext.DocumentTags by lazy {
        object: ProcessingContext.DocumentTags {
            override suspend fun plusAssign(label: String) {
                val id = client.tags.listTags().firstOrNull { it.label == label }?.id ?: return
                doc.attachTag(id)
            }

            override suspend fun minusAssign(label: String) {
                val id = client.tags.listTags().firstOrNull { it.label == label }?.id ?: return
                doc.removeTag(id)
            }
        }
    }

    override val Document.metadata: ProcessingContext.DocumentMetadata by lazy {
        object: ProcessingContext.DocumentMetadata {
            override suspend fun plusAssign(metadata: Pair<String, String>) {
                val found = doc.listMetadata().firstOrNull { it.metadataType.label == metadata.first }
                if(found == null) {
                    val id = client.metadataTypes.listMetadataTypes().firstOrNull { it.label == metadata.first }?.id
                    //TODO: doc.addMetadata(id, metadata.second)
                } else {
                    found.setValue(metadata.second)
                }
            }

            override suspend fun minusAssign(label: String) {
                doc.listMetadata().firstOrNull { it.metadataType.label == label }?.delete()
            }
        }
    }

    override val Document.cabinet: ProcessingContext.DocumentCabinets by lazy {
        object: ProcessingContext.DocumentCabinets {
            override suspend fun plusAssign(label: String) {
                client.cabinets.listCabinets().firstOrNull { it.label == label }?.addDocument(doc.id)
            }

            override suspend fun minusAssign(label: String) {
                client.cabinets.listCabinets().firstOrNull { it.label == label }?.removeDocument(doc.id)
            }
        }
    }
}