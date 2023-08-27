plugins {
    kotlin("jvm")
}

val coroutines_version: String by project
val logging_version: String by project

dependencies {
    implementation("io.github.microutils:kotlin-logging:$logging_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")

    api("io.ktor:ktor-server-core:2.2.4")
    api("io.ktor:ktor-server-netty:2.2.4")

    implementation(project(":configuration"))
    implementation(project(":worker"))
}