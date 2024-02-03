package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiDocumentCheckout
import com.virusbear.mayan.api.client.model.ApiNewDocumentCheckout
import com.virusbear.mayan.client.MayanClient
import java.net.URI
import java.nio.file.attribute.UserPrincipal
import java.time.OffsetDateTime
import com.virusbear.mayan.client.CheckoutClient

class Checkout(
    private val client: CheckoutClient,
    private val api: ApiDocumentCheckout
) {
    companion object {
        suspend fun all(client: MayanClient): List<Checkout> =
            client.checkouts.listCheckouts()

        suspend fun get(client: MayanClient, id: Int): Checkout =
            client.checkouts.getCheckout(id)

        suspend fun create(client: MayanClient, blockNewFile: Boolean, documentId: Int, expirationDateTime: OffsetDateTime): Int =
            client.checkouts.createCheckout(ApiNewDocumentCheckout(blockNewFile, documentId, expirationDateTime))
    }

    //region fields
    val checkoutDateTime: OffsetDateTime
        get() = api.checkoutDatetime!!

    val document: Document
        get() = Document(client.client.documents, api.document)

    val expirationDateTime: OffsetDateTime
        get() = api.expirationDatetime

    val id: Int
        get() = api.id!!

    val url: URI
        get() = api.url!!

    val user: User
        get() = User(client.client.users, api.user)
    //endregion

    //region operations
    suspend fun delete() {
        client.delete(this.id)
    }
    //endregion
}