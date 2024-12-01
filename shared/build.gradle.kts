plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.realm)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
                }
            }
        }
    }

    listOf(
        iosX64(), // For iOS simulators on Intel Macs
        iosArm64(), // For physical iOS devices
        //iosSimulatorArm64() // For iOS simulators on Apple Silicon Macs
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        all {
            languageSettings {
                //optIn("kotlin.RequiresOptIn")
                optIn("kotlin.uuid.ExperimentalUuidApi")
                optIn("kotlinx.datetime.format.FormatStringsInDatetimeFormats")
                optIn("io.ktor.util.InternalAPI")
            }
        }
        commonMain.dependencies {
            //put your multiplatform dependencies here
            implementation(libs.koin.core)
            implementation(libs.kvault)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.realm.base)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.content.negotiation)
            /*implementation(libs.ktor.client.ios)*/
            api(libs.logging)
            implementation(libs.stately.common)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.stately.isolate)
            implementation(libs.stately.iso.collections)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}


android {
    namespace = "com.ramo.workoutly"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildToolsVersion = "35.0.0"
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0,TimeUnit.SECONDS)
}