package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.api.DocumentTypesApi
import com.virusbear.mayan.api.client.model.ApiDocument
import com.virusbear.mayan.client.Api
import com.virusbear.mayan.client.DocumentClient
import org.w3c.dom.DocumentType
import java.net.URI
import java.time.OffsetDateTime
import java.util.*

class Document(
    private val client: DocumentClient,
    private val document: ApiDocument
) {
    // type fields
    val dateTimeCreated: OffsetDateTime
        get() = document.datetimeCreated!!

    val description: String
        get() = document.description ?: ""

    val changeTypeUrl: URI
        get() = document.documentChangeTypeUrl!!

    val type: DocumentType
        get() = DocumentType(client.api.documentTypes, document.documentType!!)

    val latestFile: DocumentFile
        get() = DocumentFile(client.api.documentFiles, document.fileLatest!!)

    val latestFileUrl: URI
        get() = document.fileListUrl!!

    val id: Int
        get() = document.id!!

    val label: String
        get() = document.label!!

    val language: String
        get() = document.language!!

    val url: URI
        get() = document.url!!

    val uuid: UUID
        get() = document.uuid!!

    val activeVersion: DocumentVersion
        get() = DocumentVersion(client.api.documentVersions, document.versionActive!!)

    val versionListUrl: URI
        get() = document.versionListUrl!!

    // type operations

    suspend fun cabinets(): List<Cabinet> =
        client.cabinets(document.id!!)

    suspend fun checkoutState(): DocumentCheckout =
        client.checkoutState(document.id!!)

    suspend fun checkin() {
        client.checkin(document.id!!)
    }

    suspend fun comments(): List<Comment> =
        client.comments(document.id!!)

    suspend fun comment(id: Int): Comment =
        client.comment(document.id!!, id)

    suspend fun duplicates(): DuplicateTargetDocument {
        TODO("Not yet implemented")
    }

    suspend fun files(): List<DocumentFile> {
        TODO("Not yet implemented")
    }

    suspend fun file(id: Int): DocumentFile {
        TODO("Not yet implemented")
    }

    suspend fun indexes(): List<Index> {
        TODO("Not yet implemented")
    }

    suspend fun metadata(): List<DocumentMetadata> {
        TODO("Not yet implemented")
    }

    suspend fun metadata(id: Int): DocumentMetadata {
        TODO("Not yet implemented")
    }

    suspend fun ocr() {
        TODO("Not yet implemented")
    }

    suspend fun resolvedSmartLinks(): List<ResolvedSmartLink> {
        TODO("Not yet implemented")
    }

    suspend fun resolvedSmartLink(id: Int): ResolvedSmartLink {
        TODO("Not yet implemented")
    }

    suspend fun resolvedWebLinks(): List<ResolvedWebLink> {
        TODO("Not yet implemented")
    }

    suspend fun resolvedWebLink(id: Int): ResolvedWebLink {
        TODO("Not yet implemented")
    }

    suspend fun signatureCaptures(): List<SignatureCapture> {
        TODO("Not yet implemented")
    }

    suspend fun signatureCapture(id: Int): SignatureCapture {
        TODO("Not yet implemented")
    }

    suspend fun tags(): List<Tag> {
        TODO("Not yet implemented")
    }

    suspend fun versions(): List<DocumentVersion> {
        TODO("Not yet implemented")
    }

    suspend fun version(id: Int): DocumentVersion {
        TODO("Not yet implemented")
    }
}