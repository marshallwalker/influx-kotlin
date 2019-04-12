import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.21"
}

group = "ca.marshallwalker"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven(url = "https://dl.bintray.com/kotlin/ktor/")
}

fun ktor(feature: String) = "io.ktor:ktor-$feature:1.1.3"

fun logback(feature: String) = "ch.qos.logback:$feature:1.2.3"

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")

    implementation(ktor("client-core"))
    implementation(ktor("client-apache"))
    implementation(ktor("client-jackson"))
    implementation(ktor("client-auth"))
    implementation(ktor("client-json"))
    implementation(ktor("client-jackson"))

    implementation("org.slf4j:slf4j-api:1.7.26")

    implementation(logback("logback-core:"))
    implementation(logback("logback-classic"))
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}