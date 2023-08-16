package com.virusbear.mayan.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocumentFilePage(
    @SerialName(value = "document_file_id") val documentFileId: String? = null,
    @SerialName(value = "document_file_url") val documentFileUrl: String? = null,
    @SerialName(value = "id") val id: Int? = null,
    @SerialName(value = "image_url") val imageUrl: String? = null,
    @SerialName(value = "page_number") val pageNumber: Int? = null,
    @SerialName(value = "url") val url: String? = null
)