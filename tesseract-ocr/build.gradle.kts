plugins {
    kotlin("jvm")
}

val coroutines_version: String by project
val logging_version: String by project

dependencies {
    implementation("io.github.microutils:kotlin-logging:$logging_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")

    implementation(project(":script-api"))
    api("net.sourceforge.tess4j:tess4j:5.10.0")
}