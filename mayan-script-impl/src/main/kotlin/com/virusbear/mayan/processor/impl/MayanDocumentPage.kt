package com.virusbear.mayan.processor.impl

import com.virusbear.mayan.client.DocumentVersionPage
import com.virusbear.mayan.processor.Page

class CachedPage(
    private val page: Page
): Page {
    private var _index: Int? = null
    private var content: String? = null
    private var image: ByteArray? = null

    override val index: Int
        get() = _index ?: page.index.also { _index = it }

    override suspend fun content(): String =
        content ?: page.content().also { content = it }

    override suspend fun image(): ByteArray =
        image ?: page.image().also { image = it }

    fun invalidate() {
        _index = null
        content = null
        image = null
    }
}

fun Page.cached(): Page =
    if(this is CachedPage) {
        this
    } else {
        CachedPage(this)
    }

class MayanDocumentPage(
    private val page: DocumentVersionPage
): Page {
    override val index: Int = page.number
    override suspend fun content(): String =
        page.content()

    override suspend fun image(): ByteArray =
        page.image()
}