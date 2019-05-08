val GROUP: String by project
val VERSION_NAME: String by project

group = GROUP
version = VERSION_NAME

plugins {
    kotlin("jvm")
    maven
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(deps.Kotlin.StdLib)
    compileOnly(deps.Kotlin.Gradle.Plugin.Api)
    compileOnly(deps.Kotlin.Compiler.Embeddable)
}

