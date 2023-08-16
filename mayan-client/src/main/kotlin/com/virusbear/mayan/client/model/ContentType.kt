package com.virusbear.mayan.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentType(
    @SerialName(value = "app_label") val appLabel: String? = null,
    @SerialName(value = "id") val id: Int? = null,
    @SerialName(value = "model") val model: String? = null
)