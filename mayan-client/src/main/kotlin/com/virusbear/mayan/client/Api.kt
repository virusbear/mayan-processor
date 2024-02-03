package com.virusbear.mayan.client

import com.virusbear.mayan.api.client.api.CabinetsApi
import com.virusbear.mayan.api.client.api.DocumentTypesApi
import com.virusbear.mayan.api.client.api.DocumentsApi
import com.virusbear.mayan.api.client.api.TagsApi
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
}