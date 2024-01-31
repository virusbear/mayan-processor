package com.virusbear.mayan.client

import com.virusbear.mayan.client.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json as J

//TODO: MayanClient really needs some attention
//TODO: shall we create a completely separate module that implements the mayan client?
class MayanClient(
    private val host: String,
    user: String,
    password: String
) {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(J {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        defaultRequest {
            url(this@MayanClient.host)
            url {
                appendPathSegments("api", "v4", "")
            }
            basicAuth(user, password)
        }
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