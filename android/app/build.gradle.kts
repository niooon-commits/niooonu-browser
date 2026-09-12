plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.niooon.browser"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.niooon.browser"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            val resolvedKeystore = listOf(
                file("${rootDir}/my-upload-key.jks"),
                file("${rootDir}/android/my-upload-key.jks"),
                file("${projectDir}/../my-upload-key.jks"),
                file("my-upload-key.jks")
            ).firstOrNull { it.exists() }?.absolutePath ?: "${rootDir}/my-upload-key.jks"

            val keystorePath = System.getenv("KEYSTORE_PATH") ?: resolvedKeystore
            storeFile = file(keystorePath)
            storePassword = System.getenv("STORE_PASSWORD") ?: "niooonu123"
            keyAlias = System.getenv("KEY_ALIAS") ?: "upload"
            keyPassword = System.getenv("KEY_PASSWORD") ?: "niooonu123"
        }
        create("debugConfig") {
            val resolvedDebug = listOf(
                file("${rootDir}/debug.keystore"),
                file("${rootDir}/android/debug.keystore"),
                file("${rootDir}/my-upload-key.jks"),
                file("${projectDir}/../my-upload-key.jks")
            ).firstOrNull { it.exists() }?.absolutePath ?: "${rootDir}/my-upload-key.jks"

            val debugPath = resolvedDebug
            storeFile = file(debugPath)
            storePassword = System.getenv("STORE_PASSWORD") ?: "niooonu123"
            keyAlias = System.getenv("KEY_ALIAS") ?: "upload"
            keyPassword = System.getenv("KEY_PASSWORD") ?: "niooonu123"
        }
    }

    buildTypes {
        release {
            isCrunchPngs = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            signingConfig = signingConfigs.getByName("debugConfig")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.5")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")

    // Image loading for articles & avatars
    implementation("io.coil-kt:coil-compose:2.7.0")

    // WebView for in-app browser functionality
    implementation("androidx.webkit:webkit:1.11.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
