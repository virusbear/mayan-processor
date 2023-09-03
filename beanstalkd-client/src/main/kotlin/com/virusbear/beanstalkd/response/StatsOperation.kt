package com.virusbear.beanstalkd.response

import com.virusbear.beanstalkd.NotFoundException
import com.virusbear.beanstalkd.operation.AbstractOperation
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

    override suspend fun read(response: Response): Result<Map<String, String>> =
        when(response) {
            is OkResponse -> Result.success(response.stats)
            is NotFoundResponse -> Result.failure(NotFoundException())
            else -> super.read(response)
        }
}