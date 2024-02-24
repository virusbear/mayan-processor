package com.virusbear.mayan.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.time.LocalDateTime

fun Api(host: String, username: String, password: String): Api {
    val client = HttpClient(CIO) {
        install(HttpCache)
        install(HttpTimeout)
        install(ContentEncoding) {
            deflate(1.0f)
            gzip(0.9f)
        }
        install(Auth) {
            basic {
                sendWithoutRequest { true }
                credentials {
                    BasicAuthCredentials(username, password)
                }
            }
        }
        install(ContentNegotiation) {
            json()
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 5)
            exponentialDelay()
        }
        defaultRequest {
            url("$host/api/v4/")
        }
    }

    return Api(client)
}

class Api internal constructor(
    internal val client: HttpClient
) {
    val documentTypes: DocumentTypesClient = DocumentTypesClient(this)
    val documents: DocumentsClient = DocumentsClient(this)
}

@Serializable
internal data class PagedResult(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: JsonArray
)

class DocumentTypesClient internal constructor(
    private val api: Api
) {
    suspend fun get(id: Int): DocumentType =
        DocumentType(
            api,
            api.client.get("document_types/$id").body<JsonObject>()
        )
}

class DocumentsClient internal constructor(
    private val api: Api
) {
    suspend fun all(): Flow<Document> =
        api.client.paged({
            val result = api.client.get("documents") {
                parameter("page", it)
            }.body<PagedResult>()

            result.results to (result.next != null)
        }) {
            Document(api, it.jsonObject)
        }

    suspend fun cabinets(id: Int): Flow<Cabinet> =
        api.client.paged({
            val response = api.client.get("documents/$id/cabients").body<PagedResult>()

            response.results to (response.next != null)
        }) {
            Cabinet(
                api,
                it.jsonObject
            )
        }

    suspend fun tags(id: Int): Flow<Tag> =
        api.client.paged({
            val response = api.client.get("documents/$id/tags").body<PagedResult>()

            response.results to (response.next != null)
        }) {
            Tag(api, it.jsonObject)
        }

    val files: DocumentFilesClient = DocumentFilesClient(api)
    val versions: DocumentVersionsClient = DocumentVersionsClient(api)
}

class DocumentFilesClient internal constructor(
    private val api: Api
) {
    suspend fun get(documentId: Int, id: Int): DocumentFile =
        DocumentFile(
            api,
            api.client.get("documents/$documentId/files/$id").body<JsonObject>()
        )

    suspend fun download(documentId: Int, id: Int): ByteArray =
        api.client.get("documents/$documentId/files/$id/download").body<ByteArray>()
}

class DocumentVersionsClient internal constructor(
    private val api: Api
) {
    val pages: DocumentVersionPagesClient = DocumentVersionPagesClient(api)

    suspend fun get(documentId: Int, id: Int): DocumentVersion =
        DocumentVersion(
            api,
            api.client.get("documents/$documentId/versions/$id").body<JsonObject>()
        )

    suspend fun listPages(documentId: Int, id: Int): Flow<DocumentVersionPage> =
        api.client.paged({
            val response = api.client.get("documents/$documentId/versions/$id/pages").body<PagedResult>()

            response.results to (response.next != null)
        }) {
            DocumentVersionPage(
                documentId,
                api,
                it.jsonObject
            )
        }
}

class DocumentVersionPagesClient internal constructor(
    private val api: Api
) {
    suspend fun content(documentId: Int, versionId: Int, id: Int): String =
        api.client.get("documents/$documentId/versions/$versionId/pages/$id/ocr").body<JsonObject>().string("content")

    suspend fun image(documentId: Int, versionId: Int, id: Int): ByteArray =
        api.client.get("documents/$documentId/versions/$versionId/pages/$id/image").body<ByteArray>()
}

internal suspend fun <R> HttpClient.paged(call: suspend (page: Int) -> Pair<JsonArray, Boolean>, map: (JsonElement) -> R): Flow<R> =
    flow {
        var page = 1

        do {
            val (results, next) = call(page++)
            results.map {
                emit(map(it))
            }
        } while(next)
    }

class Tag(
    private val api: Api,
    private val json: JsonObject
) {
    val id: Int
        get() = json.int("id")
    val label: String
        get() = json.string("label")
}

class Cabinet(
    private val api: Api,
    private val json: JsonObject
) {
    val id: Int
        get() = json.int("id")
    val label: String
        get() = json.string("label")
    val parentId: Int
        get() = json.int("parent")
    val fullPath: String
        get() = json.string("full_path")
}

class DocumentType(
    private val api: Api,
    private val json: JsonObject
) {
    val id: Int
        get() = json.int("id")
    val label: String
        get() = json.string("label")
}

class DocumentVersion(
    private val api: Api,
    private val json: JsonObject
) {
    val documentId: Int
        get() = json.int("document_id")
    val id: Int
        get() = json.int("id")

    suspend fun pages(): Flow<DocumentVersionPage> =
        api.documents.versions.listPages(documentId, id)
}

class DocumentVersionPage(
    private val documentId: Int,
    private val api: Api,
    private val json: JsonObject
) {
    val id: Int
        get() = json.int("id")
    val versionId: Int
        get() = json.int("versionId")
    val objectId: Int
        get() = json.int("objectId")
    val number: Int
        get() = json.int("page_number")

    suspend fun content(): String =
        api.documents.versions.pages.content(documentId, versionId, id)

    suspend fun image(): ByteArray =
        api.documents.versions.pages.image(documentId, versionId, id)
}

class DocumentFile(
    private val api: Api,
    private val json: JsonObject
) {
    val documentId: Int
        get() = json.int("document_id")
    val id: Int
        get() = json.int("id")
    val filename: String
        get() = json.string("filename")
    val size: Int
        get() = json.int("size")

    val mimetype: String
        get() = json.string("mimetype")

    suspend fun download(): ByteArray =
        api.documents.files.download(documentId, id)
}

class Document(
    private val api: Api,
    private val json: JsonObject
) {
    val id: Int
        get() = json.int("id")

    val label: String
        get() = json.string("label")

    val dateTimeCreated: LocalDateTime
        get() = LocalDateTime.parse(json.string("datetime_created"))

    suspend fun activeVersion(): DocumentVersion =
        api.documents.versions.get(id, json.jsonObject("version_active").int("id"))

    suspend fun latestFile(): DocumentFile =
        api.documents.files.get(id, json.jsonObject("file_latest").int("id"))

    suspend fun type(): DocumentType =
        api.documentTypes.get(json.jsonObject("document_type").int("id"))

    suspend fun cabinets(): Flow<Cabinet> =
        api.documents.cabinets(id)

    suspend fun tags(): Flow<Tag> =
        api.documents.tags(id)
}

fun JsonObject.jsonObjectOrNull(key: String): JsonObject? =
    get(key) as? JsonObject

fun JsonObject.jsonObject(key: String): JsonObject =
    jsonObjectOrNull(key)!!

fun JsonObject.intOrNull(key: String): Int? =
    (get(key) as? JsonPrimitive)?.intOrNull

fun JsonObject.int(key: String): Int =
    intOrNull(key)!!

fun JsonObject.stringOrNull(key: String): String? =
    (get(key) as? JsonPrimitive)?.content

fun JsonObject.string(key: String): String =
    stringOrNull(key)!!
