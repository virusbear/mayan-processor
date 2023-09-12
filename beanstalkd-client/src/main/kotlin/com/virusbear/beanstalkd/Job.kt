package com.virusbear.beanstalkd

import java.nio.ByteBuffer

data class Job(
    val id: UInt,
    val data: ByteBuffer
)
