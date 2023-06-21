import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.8.21"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
}

val javaVersion = JavaVersion.VERSION_17
java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

repositories {
    mavenCentral()
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "$javaVersion"
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.1")
}
