plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.0"

    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.github.gmazzo.buildconfig") version "3.1.0"
}

repositories {
    mavenCentral()
}

version "0.1-beta"

tasks {
    test {
        useJUnitPlatform {

        }
    }
}

dependencies {
    implementation(libs.kotlin.datetime)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.configyaml)

    implementation(libs.slf4j.simple)
    implementation(libs.kmongo.coroutine)

    implementation(libs.kotlinlogging)

    implementation(platform(libs.stdx.bom))
    implementation(libs.stdx.core)
    implementation(libs.stdx.envconf)

    testImplementation(libs.ktor.server.test)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.mongo.java)
}

buildConfig {
    packageName("net.whitehole.bordercontrol.generated")
    val developmentString = "DEVELOPMENT"
    val gitHash = System.getenv("GIT_HASH") ?: developmentString
    buildConfigField("String", "GIT_HASH", "\"$gitHash\"")
    buildConfigField("boolean", "DEBUG", "${gitHash == developmentString}")
    buildConfigField("String", "VERSION", "\"$version\"")
}