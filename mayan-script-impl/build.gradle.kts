plugins {
    kotlin("jvm")
}

val logging_version: String by project

dependencies {
    implementation("io.github.microutils:kotlin-logging:$logging_version")
    api(project(":script-api"))
    api(project(":script-host"))
    api(project(":mayan-client"))
    api(kotlin("reflect"))
}