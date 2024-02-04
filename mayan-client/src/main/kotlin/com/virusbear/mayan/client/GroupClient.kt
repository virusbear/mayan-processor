package com.virusbear.mayan.client

import com.virusbear.mayan.api.client.model.ApiGroup
import com.virusbear.mayan.api.client.model.ApiGroupUserAdd
import com.virusbear.mayan.api.client.model.ApiGroupUserRemove
import com.virusbear.mayan.client.model.Group
import com.virusbear.mayan.client.model.User

class GroupClient(
    val client: MayanClient,
    api: Api
): BaseClient(api) {
    suspend fun listGroups(): List<Group> =
        getPaged({
            val response = api.groups.groupsList(page = it)

            response.results to response.next
        }) {
            Group(this, it)
        }

    suspend fun getGroup(id: Int): Group =
        api.groups.groupsRead(id.toString()).let {
            Group(this, it)
        }

    internal suspend fun create(model: ApiGroup): Group =
        api.groups.groupsCreate(model).let {
            Group(this, it)
        }

    suspend fun listUsers(id: Int): List<User> =
        getPaged({
            val response = api.groups.groupsUsersList(
                groupId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            User(client.users, it)
        }

    suspend fun addUser(id: Int, userId: Int) {
        api.groups.groupsUsersAddCreate(id.toString(), ApiGroupUserAdd(userId.toString()))
    }

    suspend fun removeUser(id: Int, userId: Int) {
        api.groups.groupsUsersRemoveCreate(id.toString(), ApiGroupUserRemove(userId.toString()))
    }

    suspend fun update(id: Int, name: String): Group =
        api.groups.groupsUpdate(id.toString(), ApiGroup(name = name)).let {
            Group(this, it)
        }

    suspend fun delete(id: Int) {
        api.groups.groupsDelete(id.toString())
    }
}