rootProject.name = "shmonitoring"

include("good-api-with-kotlin")
include("type-safe-with-jooq")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("./libs.versions.toml"))
        }
    }
}
