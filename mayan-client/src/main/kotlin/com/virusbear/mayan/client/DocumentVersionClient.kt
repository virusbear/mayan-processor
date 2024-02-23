package com.virusbear.mayan.client

import com.virusbear.mayan.api.client.model.ApiDocumentVersionModificationExecute
import com.virusbear.mayan.client.model.DocumentVersionPage

class DocumentVersionClient(
    val client: MayanClient,
    api: Api
): BaseClient(api) {
    suspend fun listPages(documentId: Int, versionId: Int): List<DocumentVersionPage> =
        getPaged({
            val response = api.documents.documentsVersionsPagesList(
                documentId = documentId.toString(),
                documentVersionId = versionId.toString(),
                page = it
            )

            response.results to response.next
        }) {
            DocumentVersionPage(client.documents, documentId, it)
        }

    suspend fun getPage(documentId: Int, versionId: Int, pageId: Int): DocumentVersionPage =
        api.documents.documentsVersionsPagesRead(
            documentId = documentId.toString(),
            documentVersionId = versionId.toString(),
            documentVersionPageId = pageId.toString()
        ).let {
            DocumentVersionPage(client.documents, documentId, it)
        }

    suspend fun delete(documentId: Int, versionId: Int) {
        api.documents.documentsVersionsDelete(documentId.toString(), versionId.toString())
    }

    suspend fun export(documentId: Int, versionId: Int) {
        api.documents.documentsVersionsExportCreate(documentId.toString(), versionId.toString(), object {})
    }

    suspend fun modify(documentId: Int, versionId: Int, backend: ApiDocumentVersionModificationExecute.BackendId) {
        api.documents.documentsVersionsModifyCreate(documentId.toString(), versionId.toString(), ApiDocumentVersionModificationExecute(backend))
    }

    suspend fun submitOcr(documentId: Int, versionId: Int) {
        api.documents.documentsVersionsOcrSubmitCreate(documentId.toString(), versionId.toString())
    }
}