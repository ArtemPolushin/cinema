plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.serialization") version "1.9.21"
}

group = "ru.hse"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
}

kotlin {
    jvmToolchain(20)
}