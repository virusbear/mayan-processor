package com.virusbear.mayan.client

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
            DocumentVersionPage(it)
        }

    suspend fun getPage(documentId: Int, versionId: Int, pageId: Int): DocumentVersionPage =
        api.documents.documentsVersionsPagesRead(
            documentId = documentId.toString(),
            documentVersionId = versionId.toString(),
            documentVersionPageId = pageId.toString()
        ).let {
            DocumentVersionPage(it)
        }
}