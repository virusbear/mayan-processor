package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.NotFoundException
import com.virusbear.beanstalkd.response.NotFoundResponse
import com.virusbear.beanstalkd.response.OkResponse
import com.virusbear.beanstalkd.response.Response
import io.ktor.utils.io.core.*

class StatsOperation(
    val id: UInt? = null,
    val tube: String? = null
): AbstractOperation<Map<String, String>>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("stats")

        id?.let {
            packet.append("-job $id")
        } ?: tube?.let {
            packet.append("-tube $tube")
        }

        packet.append("\r\n")
    }

    override suspend fun readResponse(response: Response): Result<Map<String, String>> =
        when(response) {
            is OkResponse -> Result.success(response.stats)
            is NotFoundResponse -> Result.failure(NotFoundException())
            else -> super.readResponse(response)
        }
}