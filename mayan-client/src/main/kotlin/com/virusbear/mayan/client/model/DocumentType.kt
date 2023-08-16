package com.virusbear.mayan.client.model

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *
 *
 * @param label A short text identifying the document type.
 * @param deleteTimePeriod Zeitspanne nach der Dokumente dieses Typs die sich im Papierkorb befinden endgültig gelöscht werden.
 * @param deleteTimeUnit
 * @param filenameGeneratorBackend Die für die Erzeugung verantwortliche Klasse des aktuellen Dateinamens, der  für hochgeladene Dokumente verwendet wird..
 * @param filenameGeneratorBackendArguments Die Argumente für das Backend der Dateinamenserzeugung in Form eines YAML dictionary.
 * @param id
 * @param quickLabelListUrl
 * @param trashTimePeriod Zeitspanne nach der Dokumente dieses Typs in den Papierkorb verschoben werden.
 * @param trashTimeUnit
 * @param url
 */
@Serializable
data class DocumentType(
    @SerialName(value = "label") @Required val label: String,
    @SerialName(value = "delete_time_period") val deleteTimePeriod: Int? = null,
    @SerialName(value = "delete_time_unit") val deleteTimeUnit: DeleteTimeUnit? = null,
    @SerialName(value = "filename_generator_backend") val filenameGeneratorBackend: String? = null,
    @SerialName(value = "filename_generator_backend_arguments") val filenameGeneratorBackendArguments: String? = null,
    @SerialName(value = "id") val id: Int? = null,
    @SerialName(value = "quick_label_list_url") val quickLabelListUrl: String? = null,
    @SerialName(value = "trash_time_period") val trashTimePeriod: Int? = null,
    @SerialName(value = "trash_time_unit") val trashTimeUnit: TrashTimeUnit? = null,
    @SerialName(value = "url") val url: String? = null
) {
    @Serializable
    enum class DeleteTimeUnit(val value: String) {
        @SerialName(value = "days") Days("days"),
        @SerialName(value = "hours") Hours("hours"),
        @SerialName(value = "minutes") Minutes("minutes");
    }

    @Serializable
    enum class TrashTimeUnit(val value: String) {
        @SerialName(value = "days") Days("days"),
        @SerialName(value = "hours") Hours("hours"),
        @SerialName(value = "minutes") Minutes("minutes");
    }
}