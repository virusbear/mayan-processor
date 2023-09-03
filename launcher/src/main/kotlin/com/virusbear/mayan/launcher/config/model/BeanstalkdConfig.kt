package com.virusbear.mayan.launcher.config.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

data class BeanstalkdConfig(
    val host: String = "localhost",
    val port: Int = 11300,
    val queue: String? = null,
    val ttr: Duration = 1.0.minutes
)