package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.response.OkResponse
import com.virusbear.beanstalkd.response.Response
import io.ktor.utils.io.core.*

class ListTubesOperation(
    val watched: Boolean = false
): AbstractOperation<List<String>>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("list-tubes")
        if(watched) {
            packet.append("-watched")
        }
        packet.append("\r\n")
    }

    override suspend fun read(response: Response): Result<List<String>> =
        when(response) {
            is OkResponse -> Result.success(response.stats.keys.toList())
            else -> super.read(response)
        }
}