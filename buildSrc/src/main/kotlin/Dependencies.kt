import Versions.work_version

object Versions {
    val room = "2.5.0"
    val lifecycle = "2.6.0"
    val compose_ui = "1.3.3"
    val accompanist = "0.28.0"
    val datastore = "1.0.0"
    val work_version = "2.8.0"
    val material_version = "1.3.1"
}
object Dependencies {

    val appCompat = "androidx.appcompat:appcompat:1.4.1"
    val espressoAndroidTest = "androidx.test.espresso:espresso-core:3.5.1"
    val hiltAndroid = "com.google.dagger:hilt-android:2.44"
    val testRunner = "androidx.test:runner:1.5.2"
    val jupiterUnitTest = "org.junit.jupiter:junit-jupiter:5.8.1"
    val hiltCompiler = "com.google.dagger:hilt-compiler:2.44"
    val androidCore = "androidx.core:core-ktx:1.9.0"
    val datastorePrefs = "androidx.datastore:datastore-preferences:${Versions.datastore}"
    val okhttp = "com.squareup.okhttp3:okhttp:4.10.0"
    val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.10.0"
    val lifcycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    val activityCompose = "androidx.activity:activity-compose:1.6.1"
    val hiltNavigationCompose =  "androidx.hilt:hilt-navigation-compose:1.0.0"
    val navigationCompose =  "androidx.navigation:navigation-compose:2.5.3"
    val roomRuntime =  "androidx.room:room-runtime:${Versions.room}"
    val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    val work = "androidx.work:work-runtime-ktx:${Versions.work_version}"
    val androidTestWork = "androidx.work:work-testing:${Versions.work_version}"


    // optional - Kotlin Extensions and Coroutines support for Room
    val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    val viewModelSavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycle}"
    val composeUi = "androidx.compose.ui:ui:${Versions.compose_ui}"
    val composeTooling = "androidx.compose.ui:ui-tooling-preview:${Versions.compose_ui}"
    val composeMaterial = "androidx.compose.material:material:${Versions.material_version}"
    val materialExtendedIcons = "androidx.compose.material:material-icons-extended:${Versions.material_version}"
    val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    val timber =  "com.jakewharton.timber:timber:5.0.1"
    val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    val lifecycleViewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycle}"
    val gson = "com.squareup.retrofit2:converter-gson:2.9.0"
    val systemUiController =  "com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist}"
    val glide = "com.github.bumptech.glide:compose:1.0.0-alpha.1"
    val accompanistImage = "com.google.accompanist:accompanist-drawablepainter:${Versions.accompanist}"
    val junitTest = "junit:junit:4.13.2"
    val lifefycleRuntimeTesting = "androidx.lifecycle:lifecycle-runtime-testing:${Versions.lifecycle}"
    val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4"
    val truthUnitTest = "androidx.test.ext:truth:1.5.0"
    val coreAndroidTest = "androidx.arch.core:core-testing:2.2.0"
    val truthAndroidTEst = "androidx.test.ext:truth:1.5.0"
    val junitAndroidTest = "androidx.test.ext:junit:1.1.5"
    val hiltAndroidTest = "com.google.dagger:hilt-android-testing:2.28-alpha"
    val hiltAndroidCompilerTest = "com.google.dagger:hilt-android-compiler:2.38.1"
    val composeJunitTest = "androidx.compose.ui:ui-test-junit4:${Versions.compose_ui}"
    val composeUiDebugTooling = "androidx.compose.ui:ui-tooling:${Versions.compose_ui}"
    val composeUiTestManifest = "androidx.compose.ui:ui-test-manifest:${Versions.compose_ui}"
}