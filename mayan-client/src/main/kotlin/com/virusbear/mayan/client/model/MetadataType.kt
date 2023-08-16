package com.virusbear.mayan.client.model

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class MetadataType (
    @SerialName(value = "label") @Required val label: String,
    @SerialName(value = "name") @Required val name: String,
    @SerialName(value = "default") val default: String? = null,
    @SerialName(value = "id") val id: Int? = null,
    @SerialName(value = "lookup") val lookup: String? = null,
    @SerialName(value = "parser") val parser: String? = null,
    @SerialName(value = "parser_arguments") val parserArguments: String? = null,
    @SerialName(value = "url") val url: String? = null,
    @SerialName(value = "validation") val validation: String? = null,
    @SerialName(value = "validation_arguments") val validationArguments: String? = null
)