import dev.monosoul.jooq.RecommendedVersions.FLYWAY_VERSION
import dev.monosoul.jooq.RecommendedVersions.JOOQ_VERSION
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("dev.monosoul.jooq-docker") version "3.0.22"
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

    generateJooqClasses {
        basePackageName.set("dev.monosoul.shmonitoring.generated")
        usingXmlConfig()
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.1")

    implementation("org.jooq:jooq-kotlin:$JOOQ_VERSION")
    implementation("org.flywaydb:flyway-core:$FLYWAY_VERSION")

    "org.postgresql:postgresql:42.6.0"
        .also(::implementation)
        .also(::jooqCodegen)

    implementation("org.testcontainers:postgresql:1.18.3")
}
