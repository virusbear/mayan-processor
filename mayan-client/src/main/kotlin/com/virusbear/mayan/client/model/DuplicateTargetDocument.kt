package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiDuplicateTargetDocument
import com.virusbear.mayan.client.MayanClient
import java.net.URI
import java.time.OffsetDateTime
import java.util.*

class DuplicateTargetDocument(
    private val client: MayanClient,
    private val api: ApiDuplicateTargetDocument
) {
    //region fields
    val backend: String
        get() = api.backend!!

    val datetimeCreated: OffsetDateTime
        get() = api.datetimeCreated!!

    val description: String
        get() = api.description!!

    val documentChangeTypeUrl: URI
        get() = api.documentChangeTypeUrl!!

    val documentType: DocumentType
        get() = DocumentType(client.documentTypes, api.documentType!!)

    val fileLatest: DocumentFile
        get() = DocumentFile(client.documents, api.fileLatest!!)

    val fileListUrl: URI
        get() = api.fileListUrl!!

    val id: Int
        get() = api.id!!

    val label: String
        get() = api.label!!

    val language: String
        get() = api.language!!

    val url: URI
        get() = api.url!!

    val uuid: UUID
        get() = api.uuid!!

    val versionActive: DocumentVersion
        get() = DocumentVersion(client.documentVersions, api.versionActive!!)

    val versionListUrl: URI
        get() = api.versionListUrl!!
    //endregion
}