package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiDocumentVersion
import com.virusbear.mayan.api.client.model.ApiDocumentVersionModificationExecute
import com.virusbear.mayan.client.DocumentVersionClient
import java.net.URI
import java.time.OffsetDateTime

class DocumentVersion(
    private val client: DocumentVersionClient,
    private val api: ApiDocumentVersion
) {
    //region fields
    val active: Boolean
        get() = api.active!!

    val comment: String
        get() = api.comment!!

    val documentId: Int
        get() = api.documentId!!.toInt()

    val documentUrl: URI
        get() = api.documentUrl!!

    val exportUrl: URI
        get() = api.exportUrl!!

    val id: Int
        get() = api.id!!

    val pageListUrl: URI
        get() = api.pageListUrl!!

    val firstPage: DocumentVersionPage
        get() = DocumentVersionPage(client.client.documents, documentId, api.pagesFirst!!)

    val timestamp: OffsetDateTime
        get() = api.timestamp!!

    val url: URI
        get() = api.url!!
    //endregion

    //region navigate_multiple
    suspend fun listPages(): List<DocumentVersionPage> =
        client.listPages(this.documentId, this.id)
    //endregion

    //region navigate_single
    suspend fun getPage(id: Int): DocumentVersionPage =
        client.getPage(this.documentId, this.id, id)
    //endregion

    //region operations
    suspend fun delete() {
        client.delete(this.documentId, this.id)
    }

    suspend fun export() {
        client.export(this.documentId, this.id)
    }

    suspend fun modify(backend: Backend) {
        client.modify(this.documentId, this.id, backend.api)
    }

    suspend fun submitOcr() {
        client.submitOcr(this.documentId, this.id)
    }
    //endregion

    enum class Backend(
        val api: ApiDocumentVersionModificationExecute.BackendId
    ) {
        Append(ApiDocumentVersionModificationExecute.BackendId.DocumentVersionModificationPagesAppend),
        Reset(ApiDocumentVersionModificationExecute.BackendId.DocumentVersionModificationPagesReset)
    }
}