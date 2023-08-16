package com.virusbear.mayan.processor.impl

import com.virusbear.mayan.client.MayanClient
import com.virusbear.mayan.client.model.Page
import kotlinx.coroutines.runBlocking

class MayanDocumentPage(
    private val client: MayanClient,
    private val documentId: Int,
    private val page: Page
): com.virusbear.mayan.processor.Page {
    val pageNumber: Int by page::pageNumber

    override val index: Int = page.pageNumber
    override val content: String by lazy {
        runBlocking {
            client.pageContent(documentId, page.documentVersionId, page.id)
        }
    }
    override val image: ByteArray by lazy {
        runBlocking {
            client.pageImage(documentId, page.documentVersionId, page.id)
        }
    }
}