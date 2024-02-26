package com.virusbear.mayan.processor.impl

import com.virusbear.mayan.processor.Document
import com.virusbear.mayan.processor.Page
import com.virusbear.mayan.processor.Region
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.time.LocalDateTime
import com.virusbear.mayan.client.Document as ApiDocument

class CachedDocument(
    private val document: Document
): Document {
    private var id: Int? = null
    private var name: String? = null
    private var filename: String? = null
    private var filesize: Int? = null
    private var timestamp: LocalDateTime? = null
    private var type: String? = null
    private var content: String? = null
    private var cabinets: Set<String>? = null
    private var tags: Set<String>? = null
    private var metadata: Map<String, String>? = null
    private var pages: List<Page>? = null
    private var mimetype: String? = null
    private var file: ByteArray? = null

    override suspend fun id(): Int =
        id ?: document.id().also {
            id = it
        }

    override suspend fun name(): String =
        name ?: document.name().also {
            name = it
        }

    override suspend fun filename(): String =
        filename ?: document.filename().also {
            filename = it
        }

    override suspend fun filesize(): Int =
        filesize ?: document.filesize().also {
            filesize = it
        }

    override suspend fun timestamp(): LocalDateTime =
        timestamp ?: document.timestamp().also {
            timestamp = it
        }

    override suspend fun type(): String =
        type ?: document.type().also {
            type = it
        }

    override suspend fun content(): String =
        content ?: document.content().also {
            type = it
        }

    override suspend fun cabinets(): Set<String> =
        cabinets ?: document.cabinets().also {
            cabinets = it
        }

    override suspend fun tags(): Set<String> =
        tags ?: document.tags().also {
            tags = it
        }

    override suspend fun metadata(): Map<String, String> =
        metadata ?: document.metadata().also {
            metadata = it
        }

    override suspend fun pages(): List<Page> =
        pages ?: document.pages().map(Page::cached).also {
            pages = it
        }

    override suspend fun mimetype(): String =
        mimetype ?: document.mimetype().also {
            mimetype = it
        }

    override suspend fun file(): ByteArray =
        file ?: document.file().also {
            file = it
        }

    fun invalidate() {
        id = null
        name = null
        filename = null
        filesize = null
        timestamp = null
        type = null
        content = null
        cabinets = null
        tags = null
        metadata = null
        pages = null
        mimetype = null
        file = null
    }
}

fun Document.cached(): Document =
    if(this is CachedDocument) {
        this
    } else {
        CachedDocument(this)
    }

class MayanDocument(
    private val document: ApiDocument
): Document {
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
        pages().map { it.content() }.joinToString("\n")

    override suspend fun cabinets(): Set<String> =
        document.cabinets().map { it.fullPath }.toList().toSet()

    override suspend fun tags(): Set<String> =
        document.tags().map { it.label }.toList().toSet()

    override suspend fun metadata(): Map<String, String> =
        document.metadata().toList().associate { it.type().name to (it.value ?: "") }

    override suspend fun pages(): List<Page> =
        document.activeVersion().pages().map {
            MayanDocumentPage(it)
        }.toList()

    override suspend fun mimetype(): String =
        document.latestFile().mimetype

    override suspend fun file(): ByteArray =
        document.latestFile().download()
}