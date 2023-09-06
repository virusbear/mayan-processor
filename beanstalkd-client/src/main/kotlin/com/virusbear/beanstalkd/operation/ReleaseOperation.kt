package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.BuriedException
import com.virusbear.beanstalkd.NotFoundException
import com.virusbear.beanstalkd.response.BuriedResponse
import com.virusbear.beanstalkd.response.NotFoundResponse
import com.virusbear.beanstalkd.response.ReleasedResponse
import com.virusbear.beanstalkd.response.Response
import io.ktor.utils.io.core.*
import kotlin.time.Duration

class ReleaseOperation(
    val id: UInt,
    val priority: UInt,
    val delay: Duration
): AbstractOperation<Unit>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("release $id $priority, ${delay.inWholeSeconds}\r\n")
    }

    override suspend fun readResponse(response: Response): Result<Unit> =
        when(response) {
            is ReleasedResponse -> Result.success(Unit)
            is BuriedResponse -> Result.failure(BuriedException(id))
            is NotFoundResponse -> Result.failure(NotFoundException())
            else -> super.readResponse(response)
        }
}