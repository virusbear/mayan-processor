package com.virusbear.mayan.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocumentVersion(
    @SerialName(value = "active") val active: Boolean? = null,
    @SerialName(value = "comment") val comment: String? = null,
    @SerialName(value = "document_id") val documentId: String? = null,
    @SerialName(value = "document_url") val documentUrl: String? = null,
    @SerialName(value = "export_url") val exportUrl: String? = null,
    @SerialName(value = "id") val id: Int? = null,
    @SerialName(value = "page_list_url") val pageListUrl: String? = null,
    @SerialName(value = "pages_first") val pagesFirst: DocumentVersionPage? = null,
    @SerialName(value = "timestamp") val timestamp: String? = null,
    @SerialName(value = "url") val url: String? = null
)