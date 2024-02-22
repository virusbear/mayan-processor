package com.virusbear.mayan.processor.impl

import com.virusbear.mayan.processor.Document
import com.virusbear.mayan.processor.Page
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import java.time.LocalDateTime
import kotlin.reflect.KMutableProperty
import com.virusbear.mayan.client.model.Document as ApiDocument

class MayanDocument(
    private val document: ApiDocument
): Document {
    private var _indices: Set<String>? = null
    private var _tags: Set<String>? = null
    private var _metadata: Map<String, String>? = null
    private var _pages: List<Page>? = null
    private var _file: ByteArray? = null

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
        pages().joinToString("\n") { it.content }

    override suspend fun indices(): Set<String> =
        _indices ?: client.indices(document.id).map { it.value }.toSet().also { _indices = it }

    override suspend fun tags(): Set<String> =
        _tags ?: client.tags(document.id).map { it.label }.toSet().also { _tags = it }

    override suspend fun metadata(): Map<String, String> =
        _metadata ?: client.metadata(document.id).filter { it.value != null }.map { it.type.label to it.value!! }.toList().toMap().also { _metadata = it }

    override suspend fun pages(): List<Page> =
        _pages ?: client.pages(document.id).map {
            MayanDocumentPage(client, document.id, it)
        }.toList().sortedBy { it.pageNumber }.also { _pages = it }

    override suspend fun mimetype(): String =
        document.latestFile.mimetype

    override suspend fun file(): ByteArray =
        _file ?: client.fileContent(document.id, document.fileLatest?.id!!).also { _file = it }

    private infix inline fun <T> KMutableProperty<T?>.providedBy(block: () -> T): T =
        getter.call() ?: block().also { setter.call(it) }
}