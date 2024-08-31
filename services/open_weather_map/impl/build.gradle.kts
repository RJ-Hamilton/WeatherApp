plugins {
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.android.library")
    id("kotlin-kapt")
}

android {
    namespace = "com.hamilton.services.open_weather_map.impl"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(project(":services:open_weather_map:api"))

    implementation(libs.kotlin.serialization.json)

    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.converter.kotlin.serialization)
    implementation(libs.squareup.okhttp3)
    implementation(libs.squareup.okhttp3.logging)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}