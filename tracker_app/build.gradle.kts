plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
    kotlin("kapt") version "1.9.10"
}

android {
    namespace = "com.example.tracker_task"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tracker_task"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.tracker_task.CustomTestRunner"
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
    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    flavorDimensions += "tracking"

    productFlavors {
        create("prod") {
            dimension = "tracking"
            buildConfigField("int", "TRACKING_SENSITIVITY", "60")
            buildConfigField("int", "TRACKING_PERIOD", "600")
        }

        create("dev_test") {
            dimension = "tracking"
            buildConfigField("int", "TRACKING_SENSITIVITY", "10")
            buildConfigField("int", "TRACKING_PERIOD", "5")
        }
    }

    packaging {
        resources.excludes.addAll(
            listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
            )
        )
    }
}

dependencies {
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(project(mapOf("path" to ":authentication")))
    implementation("com.google.firebase:firebase-auth-ktx:22.2.0")

    //for Unit tests
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.4")
    testImplementation("androidx.test:runner:1.5.2")
    testAnnotationProcessor("com.google.dagger:dagger-compiler:2.48.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("io.mockk:mockk-android:1.13.8")
    testImplementation("io.mockk:mockk-agent:1.13.8")

    //firebase for tests
    androidTestImplementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    androidTestImplementation("com.google.firebase:firebase-firestore:24.8.1")
    androidTestImplementation("com.google.firebase:firebase-firestore-ktx:24.8.1")

    //for UI tests
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("com.google.truth:truth:1.1.4")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation(project(":authentication"))
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.44")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48.1")
    androidTestImplementation("io.mockk:mockk-android:1.13.8")
    androidTestImplementation("io.mockk:mockk-agent:1.13.8")
    debugImplementation("androidx.fragment:fragment-testing:1.6.1")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0-alpha04")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-firestore:24.8.1")
    implementation("com.google.firebase:firebase-firestore-ktx:24.8.1")

    implementation("com.google.android.gms:play-services-location:21.0.1")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")

    //RxJava and RxAndroid
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")
    annotationProcessor("com.google.dagger:hilt-compiler:2.48.1")

    //WorkManager
    implementation("androidx.work:work-runtime:2.8.1")
    annotationProcessor("androidx.hilt:hilt-compiler:1.0.0")

    implementation("androidx.hilt:hilt-work:1.0.0")

    implementation("androidx.work:work-runtime-ktx:2.8.1")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    //Room
    val room_version = "2.5.2"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")


    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore-preferences-rxjava3:1.0.0")

    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("androidx.core:core-ktx:1.12.0")

    //Navigation component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")

    // Compose
    val composeBom = platform("androidx.compose:compose-bom:2023.08.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.compose.runtime:runtime-livedata")
    // Material Design 3
    implementation("androidx.compose.material3:material3")
}

kapt {
    correctErrorTypes = true
}