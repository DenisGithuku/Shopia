plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.githukudenis.core_data"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,gradle/incremental.annotation.processors}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

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
    implementation(Dependencies.viewModelSavedState)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeTooling)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.timber)
    implementation(Dependencies.lifecycleViewModelKtx)
    implementation(Dependencies.lifecycleViewModelCompose)
    implementation(Dependencies.gson)
    implementation(Dependencies.roomRuntime)
    kapt(Dependencies.roomCompiler)
    implementation(Dependencies.roomKtx)
    implementation(Dependencies.systemUiController)
    implementation(Dependencies.glide)
    implementation(Dependencies.systemUiController)
    implementation(Dependencies.datastorePrefs)

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

    androidTestImplementation(Dependencies.espressoAndroidTest)
    androidTestImplementation(Dependencies.composeJunitTest)
    debugImplementation(Dependencies.composeUiDebugTooling)
    debugImplementation(Dependencies.composeUiTestManifest)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.gson)
}