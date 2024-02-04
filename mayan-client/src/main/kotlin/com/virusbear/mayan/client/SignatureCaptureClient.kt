package com.virusbear.mayan.client

import com.virusbear.mayan.api.client.model.ApiSignatureCapture
import com.virusbear.mayan.client.model.SignatureCapture

class SignatureCaptureClient(
    val client: MayanClient,
    api: Api
): BaseClient(api) {
    internal suspend fun update(
        documentId: Int,
        id: Int,
        model: ApiSignatureCapture
    ): SignatureCapture =
        api.documents.documentsSignatureCapturesUpdate(
            documentId.toString(),
            id.toString(),
            model
        ).let {
            SignatureCapture(this, it)
        }

    suspend fun delete(documentId: Int, id: Int) {
        api.documents.documentsSignatureCapturesDelete(documentId.toString(), id.toString())
    }
}