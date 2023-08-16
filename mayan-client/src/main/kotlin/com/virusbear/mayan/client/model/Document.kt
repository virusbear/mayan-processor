package com.virusbear.mayan.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Document(
    @SerialName(value = "datetime_created") val datetimeCreated: String? = null,
    @SerialName(value = "description") val description: String? = null,
    @SerialName(value = "document_change_type_url") val documentChangeTypeUrl: String? = null,
    @SerialName(value = "document_type") val documentType: DocumentType? = null,
    @SerialName(value = "file_latest") val fileLatest: DocumentFile? = null,
    @SerialName(value = "file_list_url") val fileListUrl: String? = null,
    @SerialName(value = "id") val id: Int,
    @SerialName(value = "label") val label: String? = null,
    @SerialName(value = "language") val language: String? = null,
    @SerialName(value = "url") val url: String? = null,
    @SerialName(value = "uuid") val uuid: String? = null,
    @SerialName(value = "version_active") val versionActive: DocumentVersion? = null,
    @SerialName(value = "version_list_url") val versionListUrl: String? = null
)