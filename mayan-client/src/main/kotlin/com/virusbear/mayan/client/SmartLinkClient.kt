package com.virusbear.mayan.client

import com.virusbear.mayan.client.model.DocumentType
import com.virusbear.mayan.client.model.SmartLinkCondition

class SmartLinkClient(
    val client: MayanClient,
    api: Api
): BaseClient(api) {
    suspend fun listConditions(id: Int): List<SmartLinkCondition> =
        getPaged({
            val response = api.smartLinks.smartLinksConditionsList(
                smartLinkId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            SmartLinkCondition(it)
        }

    suspend fun listDocumentTypes(id: Int): List<DocumentType> =
        getPaged({
            val response = api.smartLinks.smartLinksDocumentTypesList(
                smartLinkId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            DocumentType(client.documentTypes, it)
        }

    suspend fun getCondition(id: Int, conditionId: Int): SmartLinkCondition =
        api.smartLinks.smartLinksConditionsRead(id.toString(), conditionId.toString()).let {
            SmartLinkCondition(it)
        }
}