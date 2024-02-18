package com.virusbear.mayan.client

import com.virusbear.mayan.client.internal.BasicAuthorizationInterceptor
import okhttp3.OkHttpClient

class MayanClient(
    host: String,
    user: String,
    password: String
) {
    private val api = Api("$host/api/v4/", createHttpClient(user, password))

    private fun createHttpClient(username: String, password: String): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(BasicAuthorizationInterceptor(username, password))
            .build()

    val documents: DocumentClient by lazy {
        DocumentClient(this, api)
    }

    val documentTypes: DocumentTypeClient by lazy {
        DocumentTypeClient(this, api)
    }

    val tags: TagClient by lazy {
        TagClient(this, api)
    }

    val cabinets: CabinetClient by lazy {
        CabinetClient(this, api)
    }

    val comments: CommentClient by lazy {
        CommentClient(this, api)
    }

    val checkouts: CheckoutClient by lazy {
        CheckoutClient(this, api)
    }

    val users: UserClient by lazy {
        UserClient(this, api)
    }

    val groups: GroupClient by lazy {
        GroupClient(this, api)
    }

    val signatureCaptures: SignatureCaptureClient by lazy {
        SignatureCaptureClient(this, api)
    }

    val metadataTypes: MetadataTypeClient by lazy {
        MetadataTypeClient(this, api)
    }
}