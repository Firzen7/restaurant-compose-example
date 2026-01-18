plugins {
    kotlin("jvm") version "2.2.20"
    id("io.ktor.plugin") version "3.1.2"
}

group = "net.firzen.rest"
version = "1"

repositories {
    mavenCentral()
}

application {
    mainClass.set("net.firzen.rest.MainKt")
}

ktor {
    fatJar {
        archiveFileName.set("restaurants-api-v$version.jar")
    }
}

dependencies {
    // Ktor microservices framework, Apache 2.0 license
    val ktor_version = "3.1.2"
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")

    // JSON-java library, https://github.com/stleary/JSON-java
    // License: https://github.com/stleary/JSON-java/blob/master/LICENSE
    // The Software shall be used for Good, not Evil.
//    implementation("org.json:json:20231013")

    implementation("com.google.code.gson:gson:2.8.6")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}