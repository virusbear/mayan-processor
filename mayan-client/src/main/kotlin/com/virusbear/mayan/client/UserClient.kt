package com.virusbear.mayan.client

import com.virusbear.mayan.api.client.model.ApiUser
import com.virusbear.mayan.client.model.Group
import com.virusbear.mayan.client.model.User

class UserClient(
    val client: MayanClient,
    api: Api
): BaseClient(api) {
    suspend fun listUsers(): List<User> =
        getPaged({
            val response = api.users.usersList(page = it)

            response.results to response.next
        }) {
            User(this, it)
        }

    suspend fun getUser(id: Int): User =
        api.users.usersRead(id.toString()).let {
            User(this, it)
        }

    internal suspend fun create(model: ApiUser): User =
        api.users.usersCreate(model).let {
            User(this, it)
        }

    suspend fun delete(id: Int) {
        api.users.usersDelete(id.toString())
    }

    internal suspend fun update(id: Int, model: ApiUser): User =
        api.users.usersUpdate(id.toString(), model).let {
            User(this, it)
        }

    suspend fun listGroups(id: Int): List<Group> =
        getPaged({
            val response = api.users.usersGroupsList(id.toString(), page = it)

            response.results to response.next
        }) {
            Group(client.groups, it)
        }
}