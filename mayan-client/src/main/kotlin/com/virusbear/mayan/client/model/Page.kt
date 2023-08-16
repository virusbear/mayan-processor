package com.virusbear.mayan.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Page(
    @SerialName("content_type") val contentType: ContentType,
    @SerialName("document_version_id") val documentVersionId: Int,
    @SerialName("id") val id: Int,
    @SerialName("object_id") val objectId: Int,
    @SerialName("page_number") val pageNumber: Int
)