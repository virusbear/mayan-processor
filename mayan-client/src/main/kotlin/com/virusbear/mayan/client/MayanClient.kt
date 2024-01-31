package com.virusbear.mayan.client

import com.virusbear.mayan.api.client.infrastructure.ApiClient
import com.virusbear.mayan.client.internal.BasicAuthorizationInterceptor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.Response
import java.util.*

//TODO: MayanClient really needs some attention
//TODO: shall we create a completely separate module that implements the mayan client?
class MayanClient(
    host: String,
    user: String,
    password: String
) {
    private val api = ApiClient("$host/api/v4/", createHttpClient(user, password))

    private fun createHttpClient(username: String, password: String): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(BasicAuthorizationInterceptor(username, password))
            .build()

    val documentClient: DocumentClient
        get() = DocumentClient(api)

    class DocumentClient(
        private val api: ApiClient
    ): BaseApi {

    }

    //TODO: cache information like metadata types, document types etc. to increase performance.
    //TODO: have this information with a cache time of about 5min. maybe configurable using yaml parameter

    suspend fun document(id: Int): Document =
        client.get("documents/$id").body()

    suspend fun file(documentId: Int, fileId: Int): DocumentFile =
        client.get("documents/$documentId/files/$fileId").body()

    suspend fun fileContent(documentId: Int, fileId: Int): ByteArray =
        client.get("documents/$documentId/files/$fileId/download").body()

    suspend fun pages(documentId: Int): Flow<Page> =
        getPaged("documents/$documentId/versions/${activeVersionId(documentId)}/pages")

    suspend fun activeVersionId(documentId: Int): Int =
        document(documentId).versionActive!!.id!!

    suspend fun pageContent(documentId: Int, versionId: Int, pageId: Int): String {
        @Serializable
        data class Response(
            @SerialName("content") val content: String
        )

        val body = client.get("documents/$documentId/versions/$versionId/pages/$pageId/ocr").body<Response>()

        return body.content
    }

    suspend fun pageImage(documentId: Int, versionId: Int, pageId: Int): ByteArray =
        client.get("documents/$documentId/versions/$versionId/pages/$pageId/image").body()

    suspend fun tags(documentId: Int): Flow<Tag> =
        getPaged("documents/$documentId/tags")

    suspend fun indices(documentId: Int): Flow<Index> =
        getPaged("documents/$documentId/indexes")

    suspend fun metadata(documentId: Int): Flow<DocumentMetadata> =
        getPaged("documents/$documentId/metadata")

    suspend fun metadataForDocumentType(documentTypeId: Int): Flow<DocumentTypeMetadataType> =
        getPaged("document_types/$documentTypeId/metadata_types")

    suspend fun metadataTypes(): Flow<MetadataType> =
        getPaged("metadata_types")

    suspend fun metadataIdForDocument(documentId: Int, metadataLabel: String): Int? {
        val documentType = document(documentId).documentType ?: return null
        val metadataType = metadataForDocumentType(documentType.id!!).firstOrNull {
            it.metadataType.name == metadataLabel
        } ?: return null

        return metadataType.metadataType.id!!
    }

    suspend fun metadata(documentId: Int, metadataId: Int, value: String) {
        val documentMetadataId = metadata(documentId).firstOrNull {
            it.type.id == metadataId
        }?.id

        if(documentMetadataId != null) {
            @Serializable
            data class PutBody(
                @SerialName("value") val value: String
            )

            client.put("documents/$documentId/metadata/$documentMetadataId/") {
                contentType(ContentType.Application.Json)
                setBody(PutBody(value))
            }
        } else {
            @Serializable
            data class PostBody(
                @SerialName("metadata_type_id") val id: Int,
                @SerialName("value") val value: String
            )

            client.post("documents/$documentId/metadata/") {
                contentType(ContentType.Application.Json)
                setBody(PostBody(metadataId, value))
            }
        }
    }

    private suspend inline fun <reified T> getPaged(endpoint: String): Flow<T> =
        flow {
            var nextUrl: String? = endpoint

            while(nextUrl != null) {
                val response = client.get(nextUrl).body<PagedResponse<T>>()

                response.results.forEach {
                    emit(it)
                }

                nextUrl = response.next?.substringAfter(host)?.removePrefix("/")
            }
        }
}