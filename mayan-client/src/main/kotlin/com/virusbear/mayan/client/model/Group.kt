package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiGroup
import com.virusbear.mayan.client.GroupClient
import com.virusbear.mayan.client.MayanClient
import java.net.URI

class Group(
    private val client: GroupClient,
    private val api: ApiGroup
) {
    companion object {
        suspend fun all(client: MayanClient): List<Group> =
            client.groups.listGroups()

        suspend fun get(client: MayanClient, id: Int): Group =
            client.groups.getGroup(id)

        suspend fun create(client: MayanClient, name: String): Group =
            client.groups.create(ApiGroup(name = name))
    }

    //region fields
    val name: String
        get() = api.name

    val id: Int
        get() = api.id!!

    val url: URI
        get() = api.url!!

    val usersUrl: URI
        get() = api.usersUrl!!

    val usersAddUrl: URI
        get() = api.usersAddUrl!!

    val usersRemoveUrl: URI
        get() = api.usersRemoveUrl!!
    //endregion

    //region navigate_multiple
    suspend fun listUsers(): List<User> =
        client.listUsers(this.id)
    //endregion

    //region navigate_single
    //endregion

    //region operations
    suspend fun addUser(id: Int) {
        client.addUser(this.id, id)
    }

    suspend fun removeUser(id: Int) {
        client.removeUser(this.id, id)
    }

    suspend fun update(name: String): Group =
        client.update(this.id, name)

    suspend fun delete() {
        client.delete(this.id)
    }
    //endregion
}