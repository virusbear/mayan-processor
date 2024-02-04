package com.virusbear.mayan.processor.scripting

import com.virusbear.mayan.processor.MayanProcessorBuilder
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.api.implicitReceivers
import kotlin.script.experimental.api.refineConfigurationOnAnnotations
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

@KotlinScript(
    fileExtension = "mayan.kts",
    compilationConfiguration = MayanProcessorScriptConfiguration::class
)
abstract class MayanProcessorScript

annotation class Use(val library: String)

object MayanProcessorScriptConfiguration: ScriptCompilationConfiguration(
    {
        defaultImports(Use::class)
        jvm {
            //TODO: somehow we should narrow the allowed scope of scripts
            dependenciesFromCurrentContext(wholeClasspath = true)
        }
        implicitReceivers(MayanProcessorBuilder::class)
    }
)