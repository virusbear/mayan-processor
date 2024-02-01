package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.api.IndexInstancesApi
import com.virusbear.mayan.api.client.model.ApiDocument
import com.virusbear.mayan.client.MayanClient
import java.net.URL
import java.time.OffsetDateTime

class Document(
    private val config: MayanClient.ApiConfig,
    private val api: MayanClient.DocumentsClient,
    private val document: ApiDocument
) {
    val dateTimeCreated: OffsetDateTime
        get() = document.datetimeCreated!!

    val description: String
        get() = document.description ?: ""

    val changeTypeUrl: URL
        get() = document.documentChangeTypeUrl!!.toURL()

    val indices: IndexClient
        get() = api.
}