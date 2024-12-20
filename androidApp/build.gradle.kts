plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.ramo.workoutly.android"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.ramo.workoutly.android"
        minSdk = 30
        targetSdk = 35
        versionCode = 3
        versionName = "1.2"
        multiDexEnabled = true
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    splits {
        abi {
            isEnable = true
            reset()
            include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
    }
    kotlin {
        sourceSets {
            all {
                languageSettings {
                    //optIn("kotlin.RequiresOptIn")
                    optIn("androidx.compose.foundation.ExperimentalFoundationApi")
                    optIn("androidx.compose.material3.ExperimentalMaterial3Api")
                }
            }
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    buildToolsVersion = "35.0.0"
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.compose.animation)
    implementation(libs.compose.foundation)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.kotlin.reflect)
    implementation(libs.androidx.multidex)
    implementation(libs.zoomable)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.coil)
    implementation(libs.coil.video)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.dropdown)
    implementation(libs.androidx.core.splashscreen)

    /////
    //implementation(libs.play.services.auth)
    implementation(libs.androidx.health.connect)
    implementation(libs.play.services.fitness)
    implementation(libs.androidx.health.services.client)
    implementation(libs.compose.video)
    implementation(libs.androidx.media3.exoplayer) // [Required] androidx.media3 ExoPlayer dependency
    implementation(libs.androidx.media3.session) // [Required] MediaSession Extension dependency
    implementation(libs.androidx.media3.ui)
}
