package com.virusbear.mayan.client.internal

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.*

class BasicAuthorizationInterceptor(
    username: String,
    password: String
): Interceptor {
    private val header = Base64.getEncoder().encodeToString("$username:$password".toByteArray())

    override fun intercept(chain: Interceptor.Chain): Response =
        with(chain) {
            proceed(
                withHeader(
                    name = "Authorization",
                    value = "Basic $header"
                )
            )
        }

    private fun Interceptor.Chain.withHeader(name: String, value: String): Request =
        request().newBuilder().header(name, value).build()
}