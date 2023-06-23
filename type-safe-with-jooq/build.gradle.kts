import dev.monosoul.jooq.RecommendedVersions.JOOQ_VERSION
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.ForcedType

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
    usingJavaConfig {
        withName("org.jooq.codegen.KotlinGenerator")
        generate.apply {
            withKotlinNotNullRecordAttributes(true)
            database.apply {
                fun forcedType(modelName: String, type: String, columnName: String) = ForcedType()
                    .withUserType("dev.monosoul.shmonitoring.model.$modelName")
                    .withIncludeTypes(type)
                    .withIncludeExpression(".*\\.events\\.$columnName")
                    .withConverter("dev.monosoul.shmonitoring.persistence.JooqConverters.get()")

                withForcedTypes(
                    forcedType("EventId", "uuid", "id"),
                    forcedType("HostName", "text", "host_name"),
                    forcedType("ServiceName", "text", "service_name"),
                    forcedType("TeamName", "text", "owning_team_name"),
                )
            }
        }
    }
}
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.1")

    implementation("org.jooq:jooq-kotlin:$JOOQ_VERSION")

    "org.postgresql:postgresql:42.6.0"
        .also(::implementation)
        .also(::jooqCodegen)
}
