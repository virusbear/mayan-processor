package com.virusbear.mayan.client

import com.virusbear.mayan.client.model.*

class DocumentClient(
    api: Api
): BaseClient(api) {
    suspend fun document(id: Int): Document =
        Document(this, api.documents.documentsRead(id.toString()))

    suspend fun cabinets(id: Int): List<Cabinet> =
        getPaged({
            val response = api.documents.documentsCabinetsList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            Cabinet(api.cabinets, it)
        }

    suspend fun checkoutState(id: Int): DocumentCheckout =
        api.documents.documentsCheckoutRead(id.toString()).let {
            DocumentCheckout(api.checkouts, it)
        }

    suspend fun checkin(id: Int) {
        api.documents.documentsCheckoutDelete(id.toString())
    }

    suspend fun comments(id: Int): List<Comment> =
        getPaged({
            val response = api.documents.documentsCommentsList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            Comment(api.comments, it)
        }

    suspend fun comment(id: Int, comment: Int): Comment =
        api.documents.documentsCommentsRead(id.toString(), comment.toString()).let {
            Comment(api.comments, it)
        }
}

