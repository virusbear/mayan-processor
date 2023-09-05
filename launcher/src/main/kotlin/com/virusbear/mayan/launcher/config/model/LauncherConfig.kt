package com.virusbear.mayan.launcher.config.model

import com.virusbear.mayan.entrypoint.EntryPointConfig
import com.virusbear.mayan.processor.worker.WorkerConfig

data class LauncherConfig(
    val profile: Set<Profile> = setOf(Profile.Entrypoint, Profile.Worker),
    val mayan: MayanConfig,
    val entrypoint: EntryPointConfig = EntryPointConfig(),
    val worker: WorkerConfig = WorkerConfig(),
    val queue: BeanstalkdConfig = BeanstalkdConfig()
)