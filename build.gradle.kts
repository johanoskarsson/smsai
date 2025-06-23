plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "1.9.20"
    application
}

group = "com.smsai"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Ktor Server
    implementation("io.ktor:ktor-server-core-jvm:3.2.0")
    implementation("io.ktor:ktor-server-netty-jvm:3.2.0")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:3.2.0")
    implementation("io.ktor:ktor-serialization-jackson-jvm:3.2.0")
    implementation("io.ktor:ktor-server-call-logging-jvm:3.2.0")
    implementation("io.ktor:ktor-server-status-pages-jvm:3.2.0")
    
    // JSON
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    
    // Twilio SDK
    implementation("com.twilio.sdk:twilio:10.9.2")
    
    // Anthropic SDK
    implementation("com.anthropic:anthropic-java:2.1.0")

    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.14")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

application {
    mainClass.set("com.smsai.ApplicationKt")
}