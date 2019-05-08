buildscript {
    repositories {
        mavenLocal()
    }
    dependencies {
        classpath("purity:plugin:0.1.0")
    }
}

plugins {
    kotlin("jvm")
}

apply(plugin = "purity")

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(project(":annotations"))
    implementation(deps.Kotlin.StdLib)
}
