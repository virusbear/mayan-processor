package com.virusbear.mayan.processor.impl

import com.virusbear.mayan.processor.Document
import com.virusbear.mayan.processor.Page
import java.time.LocalDateTime
import com.virusbear.mayan.client.model.Document as ApiDocument

class MayanDocument(
    private val document: ApiDocument
): Document {
    private var _indices: Set<String>? = null
    private var _tags: Set<String>? = null
    private var _cabinets: Set<String>? = null
    private var _metadata: Map<String, String>? = null
    private var _pages: List<Page>? = null
    private var _file: ByteArray? = null
    private var _content: String? = null

    override suspend fun id(): Int =
        document.id
    override suspend fun name(): String =
        document.label
    override suspend fun filename(): String =
        document.latestFile.filename
    override suspend fun filesize(): Int =
        document.latestFile.size
    override suspend fun timestamp(): LocalDateTime =
        document.dateTimeCreated.toLocalDateTime()
    override suspend fun type(): String =
        document.type.label
    override suspend fun content(): String =
        ::_content providedBy { pages().map { it.content() }.joinToString("\n") }

    override suspend fun indices(): Set<String> =
        ::_indices providedBy { document.listIndexes().map { it.value }.toSet() }

    override suspend fun cabinets(): Set<String> =
        ::_cabinets providedBy { document.listCabinets().map { it.label }.toSet() }

    override suspend fun tags(): Set<String> =
        ::_tags providedBy { document.listTags().map { it.label }.toSet() }

    override suspend fun metadata(): Map<String, String> =
        ::_metadata providedBy { document.listMetadata().associate { it.metadataType.label to it.value } }

    override suspend fun pages(): List<Page> =
        ::_pages providedBy {
            document.activeVersion.listPages().map {
                MayanDocumentPage(it)
            }
        }

    override suspend fun mimetype(): String =
        document.latestFile.mimetype

    override suspend fun file(): ByteArray =
        ::_file providedBy { document.latestFile.download() }
}