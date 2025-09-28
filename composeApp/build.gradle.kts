import dev.belalkhan.gradle.tasks.RebuildIosTask
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
            freeCompilerArgs += "-Xbinary=bundleId=dev.belalkhan.taletree"
        }

        iosTarget.compilations["main"].cinterops {
            val firestore by creating {
                defFile(project.file("src/iosMain/c_interop/firestore.def"))
            }
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
        }
        pod("FirebaseAuth") {
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
        pod("FirebaseFirestore") {
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
                implementation(libs.navigation.compose)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.compose.viewmodel.navigation)
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
                implementation(libs.firebase.auth)
                implementation(libs.firebase.firestore)
            }
        }
    }

    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
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
    implementation(platform(libs.firebase.bom))
    debugImplementation(compose.uiTooling)
}

tasks.register<RebuildIosTask>("rebuildIos")
tasks.register<Delete>("cleanIos") {
    group = "ios"
    description = "Clean all iOS build artifacts and Pods"
    doFirst {
        logger.lifecycle("Cleaning iOS build artifacts...")
    }
    delete(
        project.buildDir,
        project.file("Pods"),
        project.file("Podfile.lock"),
        project.file("../iosApp/Pods"),
        project.file("../iosApp/Podfile.lock"),
        project.file("../iosApp/tale-tree-kmp.xcworkspace")
    )
}
