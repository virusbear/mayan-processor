package com.virusbear.mayan.client

import com.virusbear.mayan.api.client.model.ApiComment
import com.virusbear.mayan.client.model.Comment

class CommentClient(
    val client: MayanClient,
    api: Api
): BaseClient(api) {
    suspend fun delete(documentId: Int, id: Int) {
        api.documents.documentsCommentsDelete(documentId.toString(), id.toString())
    }

    internal suspend fun update(documentId: Int, id: Int, model: ApiComment): Comment =
        api.documents.documentsCommentsUpdate(documentId.toString(), id.toString(), model).let {
            Comment(this, documentId, it)
        }
}