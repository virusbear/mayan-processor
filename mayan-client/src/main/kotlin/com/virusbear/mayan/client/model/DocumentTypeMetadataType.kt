package com.virusbear.mayan.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocumentTypeMetadataType(
    @SerialName("document_type") val documentType: DocumentType,
    @SerialName("metadata_type") val metadataType: MetadataType,
    @SerialName("required") val required: Boolean,
    @SerialName("id") val id: Int
)