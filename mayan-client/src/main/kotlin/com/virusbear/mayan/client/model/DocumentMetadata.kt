package com.virusbear.mayan.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocumentMetadata(
    @SerialName("document") val document: Document,
    @SerialName("metadata_type") val type: MetadataType,
    @SerialName("value") val value: String?,
    @SerialName("id") val id: Int
)