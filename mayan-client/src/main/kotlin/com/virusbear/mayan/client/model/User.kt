package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiTag
import com.virusbear.mayan.api.client.model.ApiUser
import com.virusbear.mayan.client.MayanClient
import com.virusbear.mayan.client.UserClient
import java.net.URI
import java.time.OffsetDateTime

class User(
    private val client: UserClient,
    private val api: ApiUser
) {
    companion object {
        suspend fun all(client: MayanClient): List<User> =
            client.users.listUsers()

        suspend fun get(client: MayanClient, id: Int): User =
            client.users.getUser(id)

        suspend fun create(client: MayanClient, username: String, firstName: String, lastName: String, email: String, password: String): User =
            client.users.create(
                ApiUser(
                    username = username,
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = password
                )
            )
    }

    //region fields
    val username: String
        get() = api.username

    val firstName: String
        get() = api.firstName!!

    val lastName: String
        get() = api.lastName!!

    val dateJoined: OffsetDateTime
        get() = api.dateJoined!!

    val email: String
        get() = api.email!!

    val groupsUrl: URI
        get() = api.groupsUrl!!

    val id: Int
        get() = api.id!!

    val isActive: Boolean
        get() = api.isActive!!

    val lastLogin: OffsetDateTime
        get() = api.lastLogin!!

    val password: String?
        get() = api.password

    val url: URI
        get() = api.url!!
    //endregion

    //region navigate_multiple
    suspend fun listGroups(): List<Group> =
        client.listGroups(this.id)
    //endregion

    //region navigate_single
    //endregion

    //region operations
    suspend fun update(username: String = this.username, firstName: String = this.firstName, lastName: String = this.lastName, email: String = this.email, password: String? = null): User =
        client.update(
            this.id,
            ApiUser(
                username = username,
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password
            )
        )

    suspend fun delete() {
        client.delete(this.id)
    }
    //endregion
}