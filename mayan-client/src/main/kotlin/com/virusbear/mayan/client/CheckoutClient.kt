package com.virusbear.mayan.client

import com.virusbear.mayan.api.client.model.ApiNewDocumentCheckout
import com.virusbear.mayan.client.model.Checkout

class CheckoutClient(
    val client: MayanClient,
    api: Api
): BaseClient(api) {
    suspend fun listCheckouts(): List<Checkout> =
        getPaged({
            val response = api.checkouts.checkoutsList(page = it)

            response.results to response.next
        }) {
            Checkout(this, it)
        }

    suspend fun getCheckout(id: Int): Checkout =
        api.checkouts.checkoutsCheckoutInfoRead(id.toString()).let {
            Checkout(this, it)
        }

    internal suspend fun createCheckout(model: ApiNewDocumentCheckout): Int =
        api.checkouts.checkoutsCreate(model).id ?: -1

    suspend fun delete(id: Int) {
        api.checkouts.checkoutsCheckoutInfoDelete(id.toString())
    }
}