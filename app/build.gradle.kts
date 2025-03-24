plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
//    alias(libs.plugins.kotlinx.serialization)
//    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.shopping"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.shopping"
        minSdk = 34
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        val stripeKey: String = project.findProperty("STRIPE_PUBLISHABLE_KEY") as? String ?: ""
        buildConfigField("String", "STRIPE_PUBLISHABLE_KEY", "\"$stripeKey\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.hilt.android)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.androidx.foundation.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // this ia for hillt dependency injection implementation("com.google.dagger:hilt-android:2.50")



    // this is for coil
    implementation("io.coil-kt:coil-compose:2.6.0")


//    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.0-beta06")
//
//    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
//
//
//    //this is for pager
    implementation("com.google.accompanist:accompanist-pager:0.28.0")
//
    implementation("com.google.accompanist:accompanist-pager-indicators:0.28.0")
//
//// this is for payment gateway
    implementation("com.razorpay:checkout:1.6.40")
//
//
//    //custom bottom nev bar
    implementation("com.canopas.compose-animated-navigationbar:bottombar:1.0.1")
    // stripe
    implementation ("com.stripe:stripe-android:20.48.6")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // Gson converter for Retrofit
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // (Optional) Logging interceptor to debug API calls
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")
}