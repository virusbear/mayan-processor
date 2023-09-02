package com.virusbear.beanstalkd.response

import com.virusbear.beanstalkd.Job
import io.ktor.utils.io.*
import java.nio.ByteBuffer

data class ReservedResponse(
    val job: Job
): Response {
    companion object: ResponseType {
        override val code: String = "RESERVED"
        override suspend fun read(params: List<String>, channel: ByteReadChannel): Response {
            val id = params[0].toUInt()
            val length = params[1].toInt()

            val buffer = ByteBuffer.allocate(length)
            channel.readFully(buffer)
            buffer.flip()

            channel.readPacket(2).close()

            return ReservedResponse(Job(id, buffer))
        }
    }
}