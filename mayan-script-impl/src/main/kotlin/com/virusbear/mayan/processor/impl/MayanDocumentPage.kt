package com.virusbear.mayan.processor.impl

import com.virusbear.mayan.client.DocumentVersionPage
import com.virusbear.mayan.processor.Page

class MayanDocumentPage(
    private val page: DocumentVersionPage
): Page {
    private var _content: String? = null
    private var _image: ByteArray? = null

    override val index: Int = page.number
    override suspend fun content(): String =
        ::_content providedBy { page.content() }

    override suspend fun image(): ByteArray =
        ::_image providedBy { page.image() }
}