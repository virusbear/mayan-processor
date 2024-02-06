package com.virusbear.mayan.client

import com.virusbear.mayan.api.client.api.*
import com.virusbear.mayan.client.model.SignatureCapture
import okhttp3.OkHttpClient

class Api(
    private val basePath: String,
    private val client: OkHttpClient
) {
    val documents: DocumentsApi by lazy {
        DocumentsApi(basePath, client)
    }

    val documentTypes: DocumentTypesApi by lazy {
        DocumentTypesApi(basePath, client)
    }

    val tags: TagsApi by lazy {
        TagsApi(basePath, client)
    }

    val cabinets: CabinetsApi by lazy {
        CabinetsApi(basePath, client)
    }

    val checkouts: CheckoutsApi by lazy {
        CheckoutsApi(basePath, client)
    }

    val users: UsersApi by lazy {
        UsersApi(basePath, client)
    }

    val groups: GroupsApi by lazy {
        GroupsApi(basePath, client)
    }

    val smartLinks: SmartLinksApi by lazy {
        SmartLinksApi(basePath, client)
    }
}