plugins {
    kotlin("jvm")
}

val coroutines_version: String by project
val logging_version: String by project
val ktor_version: String by project

dependencies {
    implementation("io.github.microutils:kotlin-logging:$logging_version")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    api("io.ktor:ktor-network:$ktor_version")
}