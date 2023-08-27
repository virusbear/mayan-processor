package com.virusbear.mayan.entrypoint

import com.virusbear.mayan.config.HostAndPort

data class EntryPointConfig(
    val bind: HostAndPort = HostAndPort("0.0.0.0", 4357)
)