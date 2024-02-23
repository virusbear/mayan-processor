package com.virusbear.mayan.client

import com.virusbear.mayan.api.client.model.ApiDocumentChangeType
import com.virusbear.mayan.api.client.model.ApiDocumentMetadata
import com.virusbear.mayan.client.model.*

class DocumentClient(
    val client: MayanClient,
    api: Api
): BaseClient(api) {
    suspend fun listDocuments(): List<Document> =
        getPaged({
            val response = api.documents.documentsList(page = it)

            response.results to response.next
        }) {
            Document(this, it)
        }

    suspend fun getDocument(id: Int): Document =
        Document(this, api.documents.documentsRead(id.toString()))

    suspend fun listCabinets(id: Int): List<Cabinet> =
        getPaged({
            val response = api.documents.documentsCabinetsList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            Cabinet(client.cabinets, it)
        }

    suspend fun getCheckoutState(id: Int): Checkout =
        api.documents.documentsCheckoutRead(id.toString()).let {
            Checkout(client.checkouts, it)
        }

    suspend fun deleteCheckout(id: Int) {
        api.documents.documentsCheckoutDelete(id.toString())
    }

    suspend fun deleteMetadata(id: Int, metadataId: Int) {
        api.documents.documentsMetadataDelete(id.toString(), metadataId.toString())
    }

    suspend fun setMetadataValue(id: Int, metadataId: Int, value: String) {
        api.documents.documentsMetadataPartialUpdate(id.toString(), metadataId.toString(), ApiDocumentMetadata(value = value))
    }

    suspend fun createOcrSubmit(id: Int) {
        api.documents.documentsOcrSubmitCreate(id.toString())
    }

    suspend fun changeType(id: Int, documentTypeId: Int) {
        api.documents.documentsTypeChangeCreate(id.toString(), ApiDocumentChangeType(documentTypeId.toString()))
    }

    suspend fun listComments(id: Int): List<Comment> =
        getPaged({
            val response = api.documents.documentsCommentsList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            Comment(client.comments, id, it)
        }

    suspend fun getComment(id: Int, comment: Int): Comment =
        api.documents.documentsCommentsRead(id.toString(), comment.toString()).let {
            Comment(client.comments, id, it)
        }

    suspend fun listDuplicates(id: Int): List<DuplicateTargetDocument> =
        getPaged({
            val response = api.documents.documentsDuplicatesList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            DuplicateTargetDocument(client, it)
        }

    suspend fun listFiles(id: Int): List<DocumentFile> =
        getPaged({
            val response = api.documents.documentsFilesList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            DocumentFile(this, it)
        }

    suspend fun getFile(id: Int, fileId: Int): DocumentFile =
        api.documents.documentsFilesRead(
            documentId = id.toString(),
            documentFileId = fileId.toString()
        ).let {
            DocumentFile(this, it)
        }

    suspend fun downloadFile(id: Int, fileId: Int): ByteArray =
        api.documents.documentsFilesDownloadRead(id.toString(), fileId.toString()).run {
            readBytes().also { delete() }
        }

    suspend fun listDocumentFilePages(id: Int, fileId: Int): List<DocumentFilePage> =
        getPaged({
            val response = api.documents.documentsFilesPagesList(id.toString(), fileId.toString(), page = it)

            response.results to response.next
        }) {
            DocumentFilePage(this, id, it)
        }

    suspend fun downloadFilePageImage(id: Int, fileId: Int, pageId: Int): ByteArray =
        api.documents.documentsFilesPagesImageRead(id.toString(), fileId.toString(), pageId.toString()).run {
            readBytes().also { delete() }
        }

    suspend fun getFilePageContent(id: Int, fileId: Int, pageId: Int): String =
        api.documents.documentsFilesPagesContentRead(id.toString(), fileId.toString(), pageId.toString()).content ?: ""

    suspend fun listIndexes(id: Int): List<Index> =
        getPaged({
            val response = api.documents.documentsIndexesList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            Index(it)
        }

    suspend fun listMetadata(id: Int): List<DocumentMetadata> =
        getPaged({
            val response = api.documents.documentsMetadataList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            DocumentMetadata(client, it)
        }

    suspend fun getMetadata(id: Int, metadataId: Int): DocumentMetadata =
        api.documents.documentsMetadataRead(
            documentId = id.toString(),
            metadataId = metadataId.toString()
        ).let {
            DocumentMetadata(client, it)
        }

    suspend fun listResolvedSmartLinks(id: Int): List<ResolvedSmartLink> =
        getPaged({
            val response = api.documents.documentsResolvedSmartLinksList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            ResolvedSmartLink(it)
        }

    suspend fun listResolvedWebLinks(id: Int): List<ResolvedWebLink> =
        getPaged({
            val response = api.documents.documentsResolvedWebLinksList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            ResolvedWebLink(it)
        }

    suspend fun listSignatureCaptures(id: Int): List<SignatureCapture> =
        getPaged({
            val response = api.documents.documentsSignatureCapturesList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            SignatureCapture(client.signatureCaptures, it)
        }

    suspend fun listTags(id: Int): List<Tag> =
        getPaged({
            val response = api.documents.documentsTagsList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            Tag(client.tags, it)
        }

    suspend fun listVersions(id: Int): List<DocumentVersion> =
        getPaged({
            val response = api.documents.documentsVersionsList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            DocumentVersion(client.documentVersions, it)
        }

    suspend fun getResolvedSmartLink(id: Int, linkId: Int): ResolvedSmartLink =
        api.documents.documentsResolvedSmartLinksRead(id.toString(), linkId.toString()).let {
            ResolvedSmartLink(it)
        }

    suspend fun getResolvedWebLink(id: Int, linkId: Int): ResolvedWebLink =
        api.documents.documentsResolvedWebLinksRead(id.toString(), linkId.toString()).let {
            ResolvedWebLink(it)
        }

    suspend fun getSignatureCapture(id: Int, signatureId: Int): SignatureCapture =
        api.documents.documentsSignatureCapturesRead(id.toString(), signatureId.toString()).let {
            SignatureCapture(client.signatureCaptures, it)
        }

    suspend fun getVersion(id: Int, versionId: Int): DocumentVersion =
        api.documents.documentsVersionsRead(id.toString(), versionId.toString()).let {
            DocumentVersion(client.documentVersions, it)
        }

    suspend fun deleteVersionPage(id: Int, versionId: Int, pageId: Int) {
        api.documents.documentsVersionsPagesDelete(id.toString(), versionId.toString(), pageId.toString())
    }

    suspend fun downloadVersionPageImage(id: Int, versionId: Int, pageId: Int): ByteArray =
        api.documents.documentsVersionsPagesImageRead(id.toString(), versionId.toString(), pageId.toString()).run {
            readBytes().also { delete() }
        }

    suspend fun getVersionPageContent(id: Int, versionId: Int, pageId: Int): String =
        api.documents.documentsVersionsPagesOcrRead(id.toString(), versionId.toString(), pageId.toString()).content ?: ""
}

