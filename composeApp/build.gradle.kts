import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.google.gms.google.services)
    id("org.jetbrains.kotlin.native.cocoapods")
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = false // dynamic framework is required for CocoaPods + Firebase
        }
    }

    cocoapods {
        summary = "Tale Tree Multiplatform App"
        homepage = "https://www.simplifiedcoding.net"
        ios.deploymentTarget = "14.1"
        version = "1.0.0"
        podfile = project.file("../iosApp/Podfile")

        framework {
            baseName = "ComposeApp"
            isStatic = false // dynamic framework required for CocoaPods + Firebase
        }

        // CocoaPods dependencies
        pod("FirebaseCore") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            //extraOpts += listOf("-Xbinary=bundleId=dev.belalkhan.taletree")
        }
        pod("FirebaseAuth") {
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }



    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.0-rc02")
                implementation("io.insert-koin:koin-compose:4.1.1")
                implementation("io.insert-koin:koin-compose-viewmodel:4.1.1")
                implementation("io.insert-koin:koin-compose-viewmodel-navigation:4.1.1")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
                implementation("com.google.firebase:firebase-auth-ktx:23.2.1")
            }
        }
    }
}

android {
    namespace = "dev.belalkhan.taletree"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "dev.belalkhan.taletree"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
