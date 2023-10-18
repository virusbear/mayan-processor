plugins {
    kotlin("jvm") version "1.9.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.1" apply false
}

subprojects {
    group = "com.virusbear"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
