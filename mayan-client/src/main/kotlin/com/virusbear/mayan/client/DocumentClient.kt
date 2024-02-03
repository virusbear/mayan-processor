package com.virusbear.mayan.client

import com.virusbear.mayan.client.model.*
import java.security.Signature

class DocumentClient(
    val client: MayanClient,
    api: Api
): BaseClient(api) {
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
            Cabinet(api.cabinets, it)
        }

    suspend fun getCheckoutState(id: Int): DocumentCheckout =
        api.documents.documentsCheckoutRead(id.toString()).let {
            DocumentCheckout(api.checkouts, it)
        }

    suspend fun deleteCheckout(id: Int) {
        api.documents.documentsCheckoutDelete(id.toString())
    }

    suspend fun createOcrSubmit(id: Int) {
        api.documents.documentsOcrSubmitCreate(id.toString())
    }

    suspend fun listComments(id: Int): List<Comment> =
        getPaged({
            val response = api.documents.documentsCommentsList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            Comment(api.comments, it)
        }

    suspend fun getComment(id: Int, comment: Int): Comment =
        api.documents.documentsCommentsRead(id.toString(), comment.toString()).let {
            Comment(api.comments, it)
        }

    suspend fun listDuplicates(id: Int): List<DuplicateTargetDocument> =
        getPaged({
            val response = api.documents.documentsDuplicatesList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            DuplicateTargetDocument(api.duplicates, it)
        }

    suspend fun listFiles(id: Int): List<DocumentFile> =
        getPaged({
            val response = api.documents.documentsFilesList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            DocumentFile(api.files, it)
        }

    suspend fun getFile(id: Int, fileId: Int): DocumentFile =
        api.documents.documentsFilesRead(
            documentId = id.toString(),
            documentFileId = fileId.toString()
        ).let {
            DocumentFile(api.files, it)
        }

    suspend fun listIndexes(id: Int): List<Index> =
        getPaged({
            val response = api.documents.documentsIndexesList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            Index(api.indexes, it)
        }

    suspend fun listMetadata(id: Int): List<DocumentMetadata> =
        getPaged({
            val response = api.documents.documentsMetadataList(
                documentId = it.toString(),
                page = it
            )

            response.results to response.next
        }) {
            DocumentMetadata(api.metadata, it)
        }

    suspend fun getMetadata(id: Int, metadataId: Int): DocumentMetadata =
        api.documents.documentsMetadataRead(
            documentId = id.toString(),
            metadataId = metadataId.toString()
        ).let {
            DocumentMetadata(api.metadata, it)
        }

    suspend fun listResolvedSmartLinks(id: Int): List<ResolvedSmartLink> =
        getPaged({
            val response = api.documents.documentsResolvedSmartLinksList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            ResolvedSmartLink(api.smartLinks, it)
        }

    suspend fun listResolvedWebLinks(id: Int): List<ResolvedWebLink> =
        getPaged({
            val response = api.documents.documentsResolvedWebLinksList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            ResolvedWebLink(api.webLinks, it)
        }

    suspend fun listSignatureCaptures(id: Int): List<SignatureCapture> =
        getPaged({
            val response = api.documents.documentsSignatureCapturesList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            SignatureCapture(api.signatureCaptures, it)
        }

    suspend fun listTags(id: Int): List<Tag> =
        getPaged({
            val response = api.documents.documentsTagsList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            Tag(api.tags, it)
        }

    suspend fun listVersions(id: Int): List<DocumentVersion> =
        getPaged({
            val response = api.documents.documentsVersionsList(
                documentId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            DocumentVersion(api.documentVersions, it)
        }

    suspend fun getResolvedSmartLink(id: Int, linkId: Int): ResolvedSmartLink =
        api.documents.documentsResolvedSmartLinksRead(id.toString(), linkId.toString()).let {
            ResolvedSmartLink(api.smartLinks, it)
        }

    suspend fun getResolvedWebLink(id: Int, linkId: Int): ResolvedWebLink =
        api.documents.documentsResolvedWebLinksRead(id.toString(), linkId.toString()).let {
            ResolvedWebLink(api.webLinks, it)
        }

    suspend fun getSignatureCapture(id: Int, signatureId: Int): SignatureCapture =
        api.documents.documentsResolvedWebLinksRead(id.toString(), signatureId.toString()).let {
            SignatureCapture(api.signatureCaptures, it)
        }

    suspend fun getVersion(id: Int, versionId: Int): DocumentVersion =
        api.documents.documentsVersionsRead(id.toString(), versionId.toString()).let {
            DocumentVersion(api.documentVersions, it)
        }
}

