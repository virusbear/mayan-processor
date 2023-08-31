plugins {
    kotlin("jvm")
}

val coroutines_version: String by project
val logging_version: String by project
val ktor_version: String by project

dependencies {
    implementation("io.github.microutils:kotlin-logging:$logging_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")

    api("io.ktor:ktor-server-core:$ktor_version")
    api("io.ktor:ktor-server-netty:$ktor_version")

    implementation(project(":configuration"))
    implementation(project(":worker"))
}