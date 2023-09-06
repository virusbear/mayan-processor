package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.DeadlineSoonException
import com.virusbear.beanstalkd.Job
import com.virusbear.beanstalkd.NotFoundException
import com.virusbear.beanstalkd.TimedOutException
import com.virusbear.beanstalkd.response.*
import io.ktor.utils.io.core.*
import kotlin.time.Duration

class ReserveOperation(
    val timeout: Duration? = null,
    val jobId: UInt? = null
): AbstractOperation<Job>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("reserve")

        timeout?.let {
            packet.append("-with-timeout ${timeout.inWholeSeconds}")
        } ?: jobId?.let {
            packet.append("-job $jobId")
        }

        packet.append("\r\n")
    }

    override suspend fun readResponse(response: Response): Result<Job> =
        when(response) {
            is ReservedResponse -> Result.success(response.job)
            is DeadlineSoonResponse -> Result.failure(DeadlineSoonException())
            is TimedOutResponse -> Result.failure(TimedOutException())
            is NotFoundResponse -> Result.failure(NotFoundException())
            else -> super.readResponse(response)
        }
}