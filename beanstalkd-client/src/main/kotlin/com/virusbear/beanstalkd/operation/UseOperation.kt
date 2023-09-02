package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.response.Response
import com.virusbear.beanstalkd.response.UsingResponse
import io.ktor.utils.io.core.*

class UseOperation(
    private val tube: String
): AbstractOperation<String>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("use $tube\r\n")
    }

    override suspend fun read(response: Response): Result<String> =
        when(response) {
            is UsingResponse -> Result.success(response.tube)
            else -> super.read(response)
        }
}