package com.virusbear.mayan.processor.worker

import java.io.File

data class WorkerConfig(
    val workerThreads: Int = 1,
    val parallelism: Int = workerThreads,
    val maxAttempts: Int = 3,
    val scriptPath: File = File("./scripts"),
    val libraryPath: File = File("./lib"),
    val disabledModules: Set<String> = emptySet(),
    val watch: Boolean = true,
    val tesseractDataPath: File = File("./tesseract")
)