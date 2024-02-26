package com.virusbear.mayan.processor.impl

import com.virusbear.mayan.client.MayanApi
import com.virusbear.mayan.processor.Document
import com.virusbear.mayan.processor.DocumentContentProvider
import com.virusbear.mayan.processor.ProcessingContext
import kotlinx.coroutines.flow.firstOrNull
import com.virusbear.mayan.client.Document as ApiDocument

class MayanApiProcessingContext(
    private val api: MayanApi,
    private val doc: ApiDocument,
    private val contentProvider: DocumentContentProvider
): ProcessingContext, DocumentContentProvider by contentProvider {
    override val document: Document by lazy {
        MayanDocument(doc).cached()
    }

    init {
        contentProvider.withDocument(document)
    }

    override suspend fun type(label: String) {
        val id = api.documentTypes.find(label = label)?.id ?: return
        doc.changeType(id)
        invalidate()
    }

    override val Document.tags: ProcessingContext.DocumentTags by lazy {
        object: ProcessingContext.DocumentTags {
            override suspend fun plusAssign(label: String) {
                val id = api.tags.find(label = label)?.id ?: return
                doc.attachTag(id)
                invalidate()
            }

            override suspend fun minusAssign(label: String) {
                val id = api.tags.find(label = label)?.id ?: return
                doc.removeTag(id)
                invalidate()
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
                invalidate()
            }

            override suspend fun minusAssign(label: String) {
                doc.findMetadata(label)?.delete()
                invalidate()
            }
        }
    }

    override val Document.cabinets: ProcessingContext.DocumentCabinets by lazy {
        object: ProcessingContext.DocumentCabinets {
            override suspend fun plusAssign(label: String) {
                api.cabinets.find(fullPath = label)?.addDocument(doc.id)
                invalidate()
            }

            override suspend fun minusAssign(label: String) {
                api.cabinets.find(fullPath = label)?.removeDocument(doc.id)
                invalidate()
            }
        }
    }

    private fun invalidate() {
        (document as? CachedDocument)?.invalidate()
    }
}