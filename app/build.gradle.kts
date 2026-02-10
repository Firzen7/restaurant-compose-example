plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "net.firzen.android.restaurantcomposeexample"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "net.firzen.android.restaurantcomposeexample"
        minSdk = 26
        targetSdk = 36
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

val hilt_version = "2.57.1"

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material)

    // Compose ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // still part of Retrofit logic - GSON is used to (de)serialize DB entities
    implementation("com.google.code.gson:gson:2.8.6")
    // this allows Retrofit to use GSON
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Timber logging, https://github.com/JakeWharton/timber, Apache 2.0 license
    implementation("com.jakewharton.timber:timber:5.0.1")

    // AndroidX navigation for Compose
    implementation("androidx.navigation:navigation-compose:2.9.6")

    // Room DB
    implementation("androidx.room:room-runtime:2.8.4")
    kapt("androidx.room:room-compiler:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4")

    // Jetpack Hilt (uses Dagger under the hood)
    implementation("com.google.dagger:hilt-android:$hilt_version")
    implementation("com.google.dagger:hilt-android-gradle-plugin:$hilt_version")
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")
    // allows DI inside of composable functions
    kapt("com.google.dagger:hilt-compiler:$hilt_version")


    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
