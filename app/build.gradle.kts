plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.githukudenis.coroutinesindustrialbuild"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = "com.githukudenis.coroutinesindustrialbuild"
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,gradle/incremental.annotation.processors}"
        }
    }
}
hilt {
    enableAggregatingTask = true
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":core_data"))
    implementation(project(":feature_product"))
    implementation(project(":core_nav"))
    implementation(Dependencies.live_data)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.hiltAndroid)
    implementation(Dependencies.testRunner)
    kapt(Dependencies.hiltCompiler)
    implementation(Dependencies.androidCore)
    implementation(Dependencies.okhttp)
    implementation(Dependencies.loggingInterceptor)
    implementation(Dependencies.lifecycleRuntime)
    implementation(Dependencies.lifecycleRuntimeCompose)
    implementation(Dependencies.activityCompose)
    implementation(Dependencies.navigationCompose)
    implementation(Dependencies.hiltNavigationCompose)
    implementation(Dependencies.roomRuntime)
    kapt(Dependencies.roomCompiler)
    implementation(Dependencies.roomKtx)
    implementation(Dependencies.viewModelSavedState)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeTooling)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.timber)
    implementation(Dependencies.lifecycleViewModelKtx)
    implementation(Dependencies.lifecycleViewModelCompose)
    implementation(Dependencies.gson)
    implementation(Dependencies.systemUiController)
    implementation(Dependencies.glide)
    implementation(Dependencies.accompanistImage)
    implementation(Dependencies.systemUiController)
    implementation(Dependencies.work)

    testImplementation(Dependencies.junitTest)
    testImplementation(Dependencies.jupiterUnitTest)
    testImplementation(Dependencies.lifefycleRuntimeTesting)
    testImplementation(Dependencies.coroutinesTest)
    testImplementation(Dependencies.truthUnitTest)


    androidTestImplementation(Dependencies.coreAndroidTest)
    androidTestImplementation(Dependencies.truthAndroidTest)
    androidTestImplementation(Dependencies.junitAndroidTest)
    androidTestImplementation(Dependencies.hiltAndroidTest)
    kaptAndroidTest(Dependencies.hiltAndroidCompilerTest)
    androidTestImplementation(Dependencies.androidTestWork)

    androidTestImplementation(Dependencies.espressoAndroidTest)
    androidTestImplementation(Dependencies.composeJunitTest)
    debugImplementation(Dependencies.composeUiDebugTooling)
    debugImplementation(Dependencies.composeUiTestManifest)
}