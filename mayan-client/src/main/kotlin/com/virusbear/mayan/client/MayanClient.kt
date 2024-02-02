package com.virusbear.mayan.client

import com.virusbear.mayan.api.client.api.DocumentsApi
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
        DocumentClient(api)
    }

    val indexes: IndexClient by lazy {
        IndexClient(api)
    }
}

class Api(
    private val basePath: String,
    private val client: OkHttpClient
) {
    val documents: DocumentsApi by lazy {
        DocumentsApi(basePath, client)
    }
}