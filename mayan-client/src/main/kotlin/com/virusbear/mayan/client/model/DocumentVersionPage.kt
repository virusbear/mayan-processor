package com.virusbear.mayan.client.model

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocumentVersionPage(
    @SerialName(value = "object_id") @Required val objectId: Int,
    @SerialName(value = "content_type") val contentType: ContentType? = null,
    @SerialName(value = "document_version_id") val documentVersionId: String? = null,
    @SerialName(value = "document_version_url") val documentVersionUrl: String? = null,
    @SerialName(value = "id") val id: Int? = null,
    @SerialName(value = "image_url") val imageUrl: String? = null,
    @SerialName(value = "page_number") val pageNumber: Int? = null,
    @SerialName(value = "url") val url: String? = null
)