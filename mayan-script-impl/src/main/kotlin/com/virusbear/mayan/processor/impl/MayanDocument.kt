package com.virusbear.mayan.processor.impl

import com.virusbear.mayan.processor.Document
import com.virusbear.mayan.processor.Page
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.time.LocalDateTime
import com.virusbear.mayan.client.Document as ApiDocument

class MayanDocument(
    private val document: ApiDocument
): Document {
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
        document.latestFile().filename
    override suspend fun filesize(): Int =
        document.latestFile().size
    override suspend fun timestamp(): LocalDateTime =
        document.dateTimeCreated
    override suspend fun type(): String =
        document.type().label
    override suspend fun content(): String =
        ::_content providedBy { pages().map { it.content() }.joinToString("\n") }

    override suspend fun cabinets(): Set<String> =
        ::_cabinets providedBy { document.cabinets().map { it.fullPath }.toList().toSet() }

    override suspend fun tags(): Set<String> =
        ::_tags providedBy { document.tags().map { it.label }.toList().toSet() }

    override suspend fun metadata(): Map<String, String> =
        ::_metadata providedBy { document.metadata().toList().associate { it.type().name to (it.value ?: "") } }

    override suspend fun pages(): List<Page> =
        ::_pages providedBy {
            document.activeVersion().pages().map {
                MayanDocumentPage(it)
            }.toList()
        }

    override suspend fun mimetype(): String =
        document.latestFile().mimetype

    override suspend fun file(): ByteArray =
        ::_file providedBy { document.latestFile().download() }
}