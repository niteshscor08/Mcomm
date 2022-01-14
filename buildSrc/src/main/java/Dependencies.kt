object Versions {
    // region kotlin
    const val kotlin_version = "1.5.31"
    const val core_ktx = "1.3.2"
    // endregion Kotlin

    // region UI
    const val life_cycle = "2.3.0"
    const val app_compat = "1.2.0"
    const val glide = "4.12.0"
    const val material = "1.3.0"
    const val constraint_layout = "2.0.4"
    const val navigation_fragment = "2.4.0-alpha10"
    const val navigation_ui = "2.4.0-alpha10"
    const val fragment_ktx = "1.2.5"
    // endregion UI

    // region Network Layer
    const val retrofit = "2.9.0"
    const val gson = "2.8.6"
    const val gson_converter = "2.9.0"
    const val okhttp_interceptor = "4.8.0"
    // endregion Network Layer

    // region AsynchronousProgramming
    const val coroutines = "1.4.1"
    // endregion AsynchronousProgramming

    // region Local Database
    const val room = "2.2.6"
    const val room_compiler = "2.2.5"
    // endregion Local Database

    // region Dependency Injection
    const val hilt = "2.38.1"
    const val hilt_viewmodels = "1.0.0-alpha03"
    const val hilt_viewmodels_compiler = "1.0.0-alpha01"
    // endregion Dependency Injection

    // region Testing
    const val junit = "4.13.2"
    const val test_ext = "1.1.2"
    const val espresso_core = "3.3.0"
    const val mockWebServer = "4.9.0"
    const val truth = "1.1.2"
    const val android_test = "1.3.0"
    const val fragment_test = "1.3.1"
    const val arch_core_testing = "2.1.0"
    const val coroutines_testing = "1.3.4"
    // endregion Testing
}

object Dependencies {
    // region Kotlin
    const val kotlin_library = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_version}"
    const val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"
    // endregion Kotlin

    // region UI
    const val lifecycleCommon = "androidx.lifecycle:lifecycle-common:${Versions.life_cycle}"
    const val lifeCycleRunTime = "androidx.lifecycle:lifecycle-runtime:${Versions.life_cycle}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.life_cycle}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val app_compat = "androidx.appcompat:appcompat:${Versions.app_compat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraint_layout = "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"
    const val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation_fragment}"
    const val navigation_ui = "androidx.navigation:navigation-ui-ktx:${Versions.navigation_ui}"
    const val fragment_ktx = "androidx.fragment:fragment-ktx:${Versions.fragment_ktx}"

    // endregion UI

    // region Network layer
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val gson = "com.google.code.gson:gson:${Versions.gson}"
    const val gson_converter = "com.squareup.retrofit2:converter-gson:${Versions.gson_converter}"
    const val okhttp3_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp_interceptor}"
    // endregion Network layer

    // region Asynchronous Programming
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    // endregion Asynchronous Programming

    // region Local Database
    const val room = "androidx.room:room-runtime:${Versions.room}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.room}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room_compiler}"
    // endregion Local Database

    // region DependencyInjection
    const val hilt_version =  "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hilt_compiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val hilt_viewmodels = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hilt_viewmodels}"
    const val hilt_viewmodels_compiler = "androidx.hilt:hilt-compiler:${Versions.hilt_viewmodels_compiler}"

    // endregion DependencyInjection

    // region Testing
    const val junit = "junit:junit:${Versions.junit}"
    const val test_ext = "androidx.test.ext:junit:${Versions.test_ext}"
    const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso_core}"
    const val espresso_contrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso_core}"
    const val mockwebserver = "com.squareup.okhttp3:mockwebserver:${Versions.mockWebServer}"
    const val hilt_android_testing = "com.google.dagger:hilt-android-testing:${Versions.hilt}"
    const val truth = "com.google.truth:truth:${Versions.truth}"
    const val android_test = "androidx.test:core-ktx:${Versions.android_test}"
    const val fragment_test = "androidx.fragment:fragment-testing:${Versions.fragment_test}"
    const val arch_core_testing = "androidx.arch.core:core-testing:${Versions.arch_core_testing}"
    const val coroutines_testing = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines_testing}"
    // endregion Testing
}