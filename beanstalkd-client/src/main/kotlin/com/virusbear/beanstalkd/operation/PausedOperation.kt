package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.NotFoundException
import com.virusbear.beanstalkd.response.NotFoundResponse
import com.virusbear.beanstalkd.response.PausedResponse
import com.virusbear.beanstalkd.response.Response
import io.ktor.utils.io.core.*
import kotlin.time.Duration

class PausedOperation(
    val tube: String,
    val delay: Duration
): AbstractOperation<Unit>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("pause-tube $tube ${delay.inWholeSeconds}\r\n")
    }

    override suspend fun read(response: Response): Result<Unit> =
        when(response) {
            is PausedResponse -> Result.success(Unit)
            is NotFoundResponse -> Result.failure(NotFoundException())
            else -> super.read(response)
        }
}