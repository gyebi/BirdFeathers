
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.gms)
    alias(libs.plugins.compose.compiler)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.birdfeathers"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.birdfeathers"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
/*
    buildscript {
        repositories {
            google()
            mavenCentral()
        }
        dependencies {
            classpath("com.google.gms:google-services:4.4.0")
        }
    }
*/

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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.android)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Firebase (BoM keeps versions aligned)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.database)
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Google Sign-In (pick ONE source of truth)
    // If you want legacy GoogleSignIn API (optional):
    // implementation("com.google.android.gms:play-services-auth:21.2.0")
    // OR keep your catalog alias — but not both:
    implementation(libs.play.services.auth)

    // Credential Manager + Google Identity (this is what you need for GoogleIdTokenCredential)
    implementation("androidx.credentials:credentials:1.2.2")
    implementation("androidx.credentials:credentials-play-services-auth:1.2.2")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")

    // Charts
    implementation("com.github.tehras:charts:0.2.4-alpha")

    // Lottie
    implementation(libs.lottie.compose)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.androidx.foundation.layout.android)
    kapt(libs.room.compiler)

    // CSV
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.7.0")

    // Icons
    implementation("androidx.compose.material:material-icons-extended")

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation("junit:junit:4.13.2")
}

