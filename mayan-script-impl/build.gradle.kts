plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":script-api"))
    api(project(":script-host"))
    api(project(":mayan-client"))
    implementation("ch.qos.logback:logback-classic:1.4.8")
}