import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinOnlyTargetPreset

val GROUP: String by project
val VERSION_NAME: String by project

group = GROUP
version = VERSION_NAME

plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

kotlin {
    presets.withType<KotlinOnlyTargetPreset<*>>().forEach { preset ->
        targetFromPreset(preset)
    }
}

