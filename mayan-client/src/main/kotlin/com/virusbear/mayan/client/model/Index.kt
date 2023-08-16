package com.virusbear.mayan.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Index(
    @SerialName("id") val id: Int,
    @SerialName("level") val level: Int,
    @SerialName("parent_id") val parentId: Int,
    @SerialName("value") val value: String
)