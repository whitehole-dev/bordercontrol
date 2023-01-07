rootProject.name = "bordercontrol"

include(
        ":bordercontrol-common",
        ":bordercontrol-api:core",
        ":bordercontrol-api:java",
        ":bordercontrol-api:kotlin",
        ":bordercontrol-api:kotlin-coroutines"
)


dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("ktor", "2.2.2")
            version("kotlin", "1.8.0")
            version("slf4j", "2.0.6")
            version("kotlin-logging", "3.0.4")
            version("stdx-bom", "1.2.1")

            library("ktor-server-core", "io.ktor", "ktor-server-core").versionRef("ktor")
            library("ktor-server-cio", "io.ktor", "ktor-server-cio").versionRef("ktor")
            library("ktor-server-test", "io.ktor", "ktor-server-test-host").versionRef("ktor")

            library("ktor-server-configyaml", "io.ktor", "ktor-server-config-yaml").versionRef("ktor")

            library("kotlin-test", "org.jetbrains.kotlin", "kotlin-test").versionRef("kotlin")

            library("slf4j-simple", "org.slf4j", "slf4j-simple").versionRef("slf4j")

            library("stdx-bom", "dev.schlaubi", "stdx-bom").versionRef("stdx-bom")
            library("stdx-core", "dev.schlaubi", "stdx-core").withoutVersion()
            library("stdx-envconf", "dev.schlaubi", "stdx-envconf").withoutVersion()

            library("kotlinlogging", "io.github.microutils", "kotlin-logging-jvm").versionRef("kotlin-logging")
        }
    }
}