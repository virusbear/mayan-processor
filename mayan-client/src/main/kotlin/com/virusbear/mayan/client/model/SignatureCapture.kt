package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiSignatureCapture
import com.virusbear.mayan.client.SignatureCaptureClient
import java.net.URI

class SignatureCapture(
    private val client: SignatureCaptureClient,
    private val api: ApiSignatureCapture
) {
    //region fields
    val data: String
        get() = api.data!!

    val documentId: Int
        get() = api.documentId!!.toInt()

    val documentUrl: URI
        get() = api.documentUrl!!

    val id: Int
        get() = api.id!!

    val imageUrl: URI
        get() = api.imageUrl!!

    val internalName: String
        get() = api.internalName

    val text: String
        get() = api.text

    val url: URI
        get() = api.url!!
    //endregion

    //region navigate_multiple

    //endregion

    //region navigate_single
    //endregion

    //region operations
    suspend fun update(data: String = this.data, internalName: String = this.internalName, text: String = this.text): SignatureCapture =
        client.update(
            this.documentId,
            this.id,
            ApiSignatureCapture(
                internalName = internalName,
                text = text,
                data = data
            )
        )

    suspend fun delete() {
        client.delete(this.documentId, this.id)
    }
    //endregion
}