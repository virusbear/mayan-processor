package com.virusbear.mayan.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    @SerialName("color") val color: String,
    @SerialName("id") val id: Int,
    @SerialName("label") val label: String
)