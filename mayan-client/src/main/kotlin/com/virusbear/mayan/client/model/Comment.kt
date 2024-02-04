package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiComment
import com.virusbear.mayan.api.client.model.ApiUser
import com.virusbear.mayan.client.CommentClient
import java.net.URI
import java.time.OffsetDateTime

class Comment(
    private val client: CommentClient,
    private val documentId: Int,
    private val api: ApiComment
) {
    //region fields
    val content: String
        get() = api.text

    val id: Int
        get() = api.id!!

    val url: URI
        get() = api.url!!

    val user: User
        get() = User(client.client.users, api.user!!)

    val submitDate: OffsetDateTime
        get() = api.submitDate!!
    //endregion

    //region operations
    suspend fun delete() {
        client.delete(documentId, this.id)
    }

    suspend fun update(content: String = this.content, username: String = this.user.username) {
        client.update(documentId, this.id, api.copy(text = content, user = ApiUser(username = username)))
    }
    //endregion
}