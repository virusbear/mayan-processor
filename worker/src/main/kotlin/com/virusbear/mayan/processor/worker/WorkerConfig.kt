package com.virusbear.mayan.processor.worker

import java.io.File

data class WorkerConfig(
    val maxAttempts: Int = 3,
    val scriptPath: File = File("./scripts"),
    val disabledModules: Set<String> = emptySet(),
    val watch: Boolean = true
)