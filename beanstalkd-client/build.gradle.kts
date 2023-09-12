import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("jvm")
    jacoco
    id("io.gitlab.arturbosch.detekt")
}

val coroutines_version: String by project
val logging_version: String by project
val ktor_version: String by project

dependencies {
    implementation("io.github.microutils:kotlin-logging:$logging_version")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    api("io.ktor:ktor-network:$ktor_version")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report

    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(false)
    }
}

detekt {
    ignoreFailures = false
    reports {
        html.required.set(false)
        md.required.set(false)
        sarif.required.set(false)
        txt.required.set(false)
        xml.required.set(true)
    }
}