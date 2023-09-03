package com.virusbear.beanstalkd.response

import io.ktor.utils.io.*

class OkResponse(
    val stats: Map<String, String>
): Response {
    companion object: ResponseType {
        override val code: String = "OK"

        override suspend fun read(params: List<String>, channel: ByteReadChannel): Response {
            val length = params[0].toInt()

            val yaml = channel.readPacket(length).readText()
            val stats = yaml.lines().filter { it != "---" && it.isNotEmpty() }.associate {
                if(it.startsWith("- ")) {
                    it.removePrefix("- ") to ""
                } else {
                    val (key, value) = it.split(": ", limit = 2)
                    key to value.removeSurrounding("\"")
                }
            }

            return OkResponse(stats)
        }
    }
}