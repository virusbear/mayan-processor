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
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.time.LocalDateTime

fun MayanApi(host: String, username: String, password: String): MayanApi {
    val client = HttpClient(CIO) {
        install(HttpRedirect) {
            checkHttpMethod = false
        }
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

    return MayanApi(client)
}

class MayanApi internal constructor(
    internal val client: HttpClient
) {
    val documentTypes: DocumentTypesClient = DocumentTypesClient(this)
    val documents: DocumentsClient = DocumentsClient(this)
    val metadataTypes: MetadataTypesClient = MetadataTypesClient(this)
    val tags: TagsClient = TagsClient(this)
    val cabinets: CabinetsClient = CabinetsClient(this)
}

@Serializable
internal data class PagedResult(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: JsonArray
)

class CabinetsClient internal constructor(
    private val api: MayanApi
) {
    suspend fun all(): Flow<Cabinet> =
        api.client.paged({
            val response = api.client.get("cabinets") {
                parameter("page", it)
            }.body<PagedResult>()

            response.results to (response.next != null)
        }) {
            Cabinet(api, it.jsonObject)
        }

    suspend fun find(fullPath: String? = null, label: String? = null): Cabinet? =
        all().let { flow ->
            fullPath?.let {
                flow.filter {
                    it.fullPath == fullPath
                }
            } ?: flow
        }.let { flow ->
            label?.let {
                flow.filter {
                    it.label == label
                }
            } ?: flow
        }.firstOrNull()

    suspend fun addDocument(id: Int, documentId: Int) {
        @Serializable
        data class CabinetDocumentAddBody(
            val document: String
        )
        api.client.post("cabinets/$id/documents/add") {
            contentType(ContentType.Application.Json)
            setBody(CabinetDocumentAddBody(documentId.toString()))
        }
    }

    suspend fun removeDocument(id: Int, documentId: Int) {
        @Serializable
        data class CabinetDocumentRemoveBody(
            val document: String
        )
        api.client.post("cabinets/$id/documents/remove") {
            contentType(ContentType.Application.Json)
            setBody(CabinetDocumentRemoveBody(documentId.toString()))
        }
    }
}

class TagsClient internal constructor(
    private val api: MayanApi
) {
    suspend fun all(): Flow<Tag> =
        api.client.paged({
            val response = api.client.get("tags") {
                parameter("page", it)
            }.body<PagedResult>()

            response.results to (response.next != null)
        }) {
            Tag(api, it.jsonObject)
        }

    suspend fun find(label: String? = null): Tag? =
        all().firstOrNull {
            it.label == label
        }
}

class MetadataTypesClient internal constructor(
    private val api: MayanApi
) {
    suspend fun all(): Flow<MetadataType> =
        api.client.paged({
            val response = api.client.get("metadata_types") {
                parameter("page", it)
            }.body<PagedResult>()

            response.results to (response.next != null)
        }) {
            MetadataType(
                api,
                it.jsonObject
            )
        }

    suspend fun get(id: Int): MetadataType =
        MetadataType(
            api,
            api.client.get("metadata_types/$id").body<JsonObject>()
        )

    suspend fun find(name: String? = null): MetadataType? =
        all().firstOrNull {
            it.name == name
        }
}

class DocumentTypesClient internal constructor(
    private val api: MayanApi
) {
    suspend fun all(): Flow<DocumentType> =
        api.client.paged({
            val response = api.client.get("document_types") {
                parameter("page", it)
            }.body<PagedResult>()

            response.results to (response.next != null)
        }) {
            DocumentType(
                api,
                it.jsonObject
            )
        }

    suspend fun get(id: Int): DocumentType =
        DocumentType(
            api,
            api.client.get("document_types/$id").body<JsonObject>()
        )

    suspend fun find(label: String? = null): DocumentType? =
        all().firstOrNull {
            it.label == label
        }
}

class DocumentsClient internal constructor(
    private val api: MayanApi
) {
    suspend fun get(id: Int): Document =
        Document(
            api,
            api.client.get("documents/$id").body<JsonObject>()
        )

    suspend fun all(): Flow<Document> =
        api.client.paged({
            val response = api.client.get("documents") {
                parameter("page", it)
            }.body<PagedResult>()

            response.results to (response.next != null)
        }) {
            Document(api, it.jsonObject)
        }

    suspend fun cabinets(id: Int): Flow<Cabinet> =
        api.client.paged({
            val response = api.client.get("documents/$id/cabients") {
                parameter("page", it)
            }.body<PagedResult>()

            response.results to (response.next != null)
        }) {
            Cabinet(
                api,
                it.jsonObject
            )
        }

    suspend fun tags(id: Int): Flow<Tag> =
        api.client.paged({
            val response = api.client.get("documents/$id/tags") {
                parameter("page", it)
            }.body<PagedResult>()

            response.results to (response.next != null)
        }) {
            Tag(api, it.jsonObject)
        }

    suspend fun metadata(id: Int): Flow<DocumentMetadata> =
        api.client.paged({
            val response = api.client.get("documents/$id/metadata") {
                parameter("page", it)
            }.body<PagedResult>()

            response.results to (response.next != null)
        }) {
            DocumentMetadata(api, it.jsonObject)
        }

    suspend fun findMetadata(id: Int, name: String? = null): DocumentMetadata? =
        metadata(id).firstOrNull {
            it.type().name == name
        }

    suspend fun changeType(id: Int, typeId: Int) {
        @Serializable
        data class DocumentChangeTypeBody(
            @SerialName("document_type_id")
            val documentTypeId: String
        )

        api.client.post("documents/$id/type/change") {
            contentType(ContentType.Application.Json)
            setBody(DocumentChangeTypeBody(typeId.toString()))
        }
    }

    suspend fun attachTag(id: Int, tagId: Int) {
        @Serializable
        data class DocumentTagAttachBody(
            val tag: String
        )

        api.client.post("documents/$id/tags/attach") {
            contentType(ContentType.Application.Json)
            setBody(DocumentTagAttachBody(tagId.toString()))
        }
    }

    suspend fun removeTag(id: Int, tagId: Int) {
        @Serializable
        data class DocumentTagRemoveBody(
            val tag: String
        )

        api.client.post("documents/$id/tags/remove") {
            contentType(ContentType.Application.Json)
            setBody(DocumentTagRemoveBody(tagId.toString()))
        }
    }

    suspend fun deleteMetadata(id: Int, metadataId: Int) {
        api.client.delete("documents/$id/metadata/$metadataId")
    }

    suspend fun setMetadataValue(id: Int, metadataId: Int, value: String?) {
        @Serializable
        data class DocumentMetadataSetValueBody(
            val value: String?
        )
        api.client.patch("documents/$id/metadata/$metadataId") {
            contentType(ContentType.Application.Json)
            setBody(DocumentMetadataSetValueBody(value))
        }
    }

    suspend fun addMetadata(id: Int, metadataTypeId: Int, value: String?) {
        @Serializable
        data class DocumentMetadataAddBody(
            @SerialName("metadata_type_id")
            val type: Int,
            val value: String?
        )
        api.client.post("documents/$id/metadata") {
            contentType(ContentType.Application.Json)
            setBody(
                DocumentMetadataAddBody(
                    metadataTypeId,
                    value
                )
            )
        }
    }

    val files: DocumentFilesClient = DocumentFilesClient(api)
    val versions: DocumentVersionsClient = DocumentVersionsClient(api)
}

class DocumentFilesClient internal constructor(
    private val api: MayanApi
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
    private val api: MayanApi
) {
    val pages: DocumentVersionPagesClient = DocumentVersionPagesClient(api)

    suspend fun get(documentId: Int, id: Int): DocumentVersion =
        DocumentVersion(
            api,
            api.client.get("documents/$documentId/versions/$id").body<JsonObject>()
        )

    suspend fun listPages(documentId: Int, id: Int): Flow<DocumentVersionPage> =
        api.client.paged({
            val response = api.client.get("documents/$documentId/versions/$id/pages") {
                parameter("page", it)
            }.body<PagedResult>()

            response.results to (response.next != null)
        }) {
            DocumentVersionPage(
                documentId,
                id,
                api,
                it.jsonObject
            )
        }
}

class DocumentVersionPagesClient internal constructor(
    private val api: MayanApi
) {
    suspend fun content(documentId: Int, versionId: Int, id: Int): String =
        api.client.get("documents/$documentId/versions/$versionId/pages/$id/ocr").body<JsonObject>().string("content")

    suspend fun image(documentId: Int, versionId: Int, id: Int): ByteArray =
        api.client.get("documents/$documentId/versions/$versionId/pages/$id/image").body<ByteArray>()
}

internal suspend fun <R> HttpClient.paged(
    call: suspend (page: Int) -> Pair<JsonArray, Boolean>,
    map: (JsonElement) -> R
): Flow<R> =
    flow {
        var page = 1

        do {
            val (results, next) = call(page++)
            results.map {
                emit(map(it))
            }
        } while (next)
    }

class MetadataType(
    private val api: MayanApi,
    private val json: JsonObject
) {
    val id: Int
        get() = json.int("id")
    val default: String?
        get() = json.stringOrNull("default")
    val label: String
        get() = json.string("label")
    val name: String
        get() = json.string("name")
}

class DocumentMetadata(
    private val api: MayanApi,
    private val json: JsonObject
) {
    val id: Int
        get() = json.int("id")
    val documentId: Int
        get() = json.jsonObject("document").int("id")

    suspend fun type(): MetadataType =
        api.metadataTypes.get(json.jsonObject("metadata_type").int("id"))

    val value: String?
        get() = json.stringOrNull("value")

    suspend fun delete() {
        api.documents.deleteMetadata(documentId, id)
    }

    suspend fun setValue(value: String?) {
        api.documents.setMetadataValue(documentId, id, value)
    }
}

class Tag(
    private val api: MayanApi,
    private val json: JsonObject
) {
    val id: Int
        get() = json.int("id")
    val label: String
        get() = json.string("label")
}

class Cabinet(
    private val api: MayanApi,
    private val json: JsonObject
) {
    val id: Int
        get() = json.int("id")
    val label: String
        get() = json.string("label")
    val parentId: Int?
        get() = json.intOrNull("parent")
    val fullPath: String
        get() = json.string("full_path")

    suspend fun addDocument(documentId: Int) {
        api.cabinets.addDocument(id, documentId)
    }

    suspend fun removeDocument(documentId: Int) {
        api.cabinets.removeDocument(id, documentId)
    }
}

class DocumentType(
    private val api: MayanApi,
    private val json: JsonObject
) {
    val id: Int
        get() = json.int("id")
    val label: String
        get() = json.string("label")
}

class DocumentVersion(
    private val api: MayanApi,
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
    private val versionId: Int,
    private val api: MayanApi,
    private val json: JsonObject
) {
    val id: Int
        get() = json.int("id")
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
    private val api: MayanApi,
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
    private val api: MayanApi,
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

    suspend fun metadata(): Flow<DocumentMetadata> =
        api.documents.metadata(id)

    suspend fun findMetadata(name: String? = null): DocumentMetadata? =
        api.documents.findMetadata(id, name)

    suspend fun changeType(typeId: Int) {
        api.documents.changeType(id, typeId)
    }

    suspend fun attachTag(tagId: Int) {
        api.documents.attachTag(id, tagId)
    }

    suspend fun removeTag(tagId: Int) {
        api.documents.removeTag(id, tagId)
    }

    suspend fun addMetadata(metadataTypeId: Int, value: String?) {
        api.documents.addMetadata(id, metadataTypeId, value)
    }
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
