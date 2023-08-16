package com.virusbear.mayan.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocumentFilePageContent(
    @SerialName(value = "content") val content: String? = null
)