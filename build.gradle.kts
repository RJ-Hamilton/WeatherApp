buildscript {
    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.kotlin.serialization.gradlePlugin)
    }
    repositories {
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.verison.catalog.update.plugin)
    id("com.google.dagger.hilt.android") version libs.versions.hilt apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

versionCatalogUpdate {
    keep {
        versions.set(listOf("compose", "kotlin"))
        keepUnusedVersions.set(true)
        keepUnusedLibraries.set(false)
        keepUnusedPlugins.set(false)
    }
}