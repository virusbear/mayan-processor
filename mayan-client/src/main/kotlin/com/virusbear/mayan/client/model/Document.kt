package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiDocument
import com.virusbear.mayan.client.DocumentClient
import java.net.URI
import java.time.OffsetDateTime
import java.util.*

class Document(
    private val client: DocumentClient,
    private val api: ApiDocument
) {
    //region fields
    val dateTimeCreated: OffsetDateTime
        get() = api.datetimeCreated!!

    val description: String
        get() = api.description ?: ""

    val changeTypeUrl: URI
        get() = api.documentChangeTypeUrl!!

    val type: DocumentType
        get() = DocumentType(client.client.documentTypes, api.documentType!!)

    val latestFile: DocumentFile
        get() = DocumentFile(client.client.documents, api.fileLatest!!)

    val latestFileUrl: URI
        get() = api.fileListUrl!!

    val id: Int
        get() = this.id

    val label: String
        get() = api.label!!

    val language: String
        get() = api.language!!

    val url: URI
        get() = api.url!!

    val uuid: UUID
        get() = api.uuid!!

    val activeVersion: DocumentVersion
        get() = DocumentVersion(client.client.documentVersions, api.versionActive!!)

    val versionListUrl: URI
        get() = api.versionListUrl!!
    //endregion

    //region navigate_multiple
    suspend fun listCabinets(): List<Cabinet> =
        client.listCabinets(this.id)

    suspend fun listComments(): List<Comment> =
        client.listComments(this.id)

    suspend fun listDuplicates(): List<DuplicateTargetDocument> =
        client.listDuplicates(this.id)

    suspend fun listIndexes(): List<Index> =
        client.listIndexes(this.id)

    suspend fun listMetadata(): List<DocumentMetadata> =
        client.listMetadata((this.id))

    suspend fun listFiles(): List<DocumentFile> =
        client.listFiles(this.id)

    suspend fun listResolvedSmartLinks(): List<ResolvedSmartLink> =
        client.listResolvedSmartLinks(this.id)

    suspend fun listResolvedWebLinks(): List<ResolvedWebLink> =
        client.listResolvedWebLinks(this.id)

    suspend fun listSignatureCaptures(): List<SignatureCapture> =
        client.listSignatureCaptures(this.id)

    suspend fun listTags(): List<Tag> =
        client.listTags(this.id)

    suspend fun listVersions(): List<DocumentVersion> =
        client.listVersions(this.id)
    //endregion

    //region navigate_single
    suspend fun getCheckoutState(): Checkout =
        client.getCheckoutState(this.id)

    suspend fun getComment(id: Int): Comment =
        client.getComment(this.id, id)

    suspend fun getFile(id: Int): DocumentFile =
        client.getFile(this.id, id)

    suspend fun getMetadata(id: Int): DocumentMetadata =
        client.getMetadata(this.id, id)

    suspend fun getResolvedSmartLink(id: Int): ResolvedSmartLink =
        client.getResolvedSmartLink(this.id, id)

    suspend fun getResolvedWebLink(id: Int): ResolvedWebLink =
        client.getResolvedWebLink(this.id, id)

    suspend fun getSignatureCapture(id: Int): SignatureCapture =
        client.getSignatureCapture(this.id, id)

    suspend fun getVersion(id: Int): DocumentVersion =
        client.getVersion(this.id, id)
    //endregion

    //region operations
    suspend fun checkin() {
        client.deleteCheckout(this.id)
    }

    suspend fun submitForOcr() =
        client.createOcrSubmit(this.id)
    //endregion
}