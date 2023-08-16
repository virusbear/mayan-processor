package com.virusbear.mayan.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocumentFile (
    @SerialName(value = "checksum") val checksum: String? = null,
    @SerialName(value = "comment") val comment: String? = null,
    @SerialName(value = "document_id") val documentId: String? = null,
    @SerialName(value = "document_url") val documentUrl: String? = null,
    @SerialName(value = "download_url") val downloadUrl: String? = null,
    @SerialName(value = "encoding") val encoding: String? = null,
    @SerialName(value = "file") val file: String? = null,
    @SerialName(value = "filename") val filename: String? = null,
    @SerialName(value = "id") val id: Int? = null,
    @SerialName(value = "mimetype") val mimetype: String? = null,
    @SerialName(value = "page_list_url") val pageListUrl: String? = null,
    @SerialName(value = "pages_first") val pagesFirst: DocumentFilePage? = null,
    @SerialName(value = "size") val propertySize: Int? = null,
    @SerialName(value = "timestamp") val timestamp: String? = null,
    @SerialName(value = "url") val url: String? = null
)