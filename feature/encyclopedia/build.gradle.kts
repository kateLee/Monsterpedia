import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.katelee.monsterpedia.feature.encyclopedia"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    compileOptions {
        //JUnit 6 要求 Java 17+
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        // Extension level
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
            optIn.add("kotlin.RequiresOptIn")
        }

        testOptions {
            unitTests {
                isIncludeAndroidResources = true
                isReturnDefaultValues = true
                all {
                    it.useJUnitPlatform() // JUnit 5,6 用這個
                }
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.compose.material.icons.extended)

    implementation(libs.hilt.android)
    implementation(libs.androidx.palette.ktx)
    ksp(libs.hilt.compiler)

    api(libs.coil.compose)
    api(libs.coil.network.okhttp)

    implementation(project(":domain"))

    // ==================== Unit Test (JUnit 6) ====================
    // Aligning all JUnit dependencies to the same version
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)

    // ==================== Instrumented Test (JUnit 4) ====================
    testImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}