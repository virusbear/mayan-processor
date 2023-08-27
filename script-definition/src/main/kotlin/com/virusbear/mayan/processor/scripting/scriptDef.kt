package com.virusbear.mayan.processor.scripting

import com.virusbear.mayan.processor.MayanProcessorBuilder
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.api.implicitReceivers
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

@KotlinScript(
    fileExtension = "mayan.kts",
    compilationConfiguration = MayanProcessorScriptConfiguration::class
)
abstract class MayanProcessorScript

object MayanProcessorScriptConfiguration: ScriptCompilationConfiguration(
    {
        defaultImports()
        jvm {
            dependenciesFromCurrentContext(wholeClasspath = true)
        }
        implicitReceivers(MayanProcessorBuilder::class)
    }
)