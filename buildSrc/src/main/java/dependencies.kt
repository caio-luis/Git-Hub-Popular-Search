object CoreDependencies {
    const val ktxCore = "androidx.core:core-ktx:${KotlinCoreVersions.ktxCoreVersion}"
    const val kotlinStdLib =
        "org.jetbrains.kotlin:kotlin-stdlib:${KotlinCoreVersions.kotlinVersion}"
    const val appCompat = "androidx.appcompat:appcompat:${KotlinCoreVersions.appCompatVersion}"
    const val gradleAndroid = "com.android.tools.build:gradle:${BuildVersions.androidGradle}"
    const val gradleKotlin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${KotlinCoreVersions.kotlinVersion}"
}

object TestDependencies {
    const val jUnit = "androidx.test.ext:junit:${TestVersions.androidxJunit}"
    const val assertJ = "org.assertj:assertj-core:${TestVersions.assertJ}"
    const val androidTestCore = "androidx.test:core:${TestVersions.androidTestCore}"
    const val androidTestRunner = "androidx.test:runner:${TestVersions.androidTestRunner}"
    const val androidTestRules = "androidx.test:rules:${TestVersions.androidTestRules}"
    const val espresso = "androidx.test.espresso:espresso-core:${TestVersions.espresso}"
    const val coreTesting = "androidx.arch.core:core-testing:${TestVersions.coreTesting}"
    const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${TestVersions.kotlinTest}"
    const val kotlinxCoroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${TestVersions.kotlinxCoroutines}"
    const val robolectric = "org.robolectric:robolectric:${TestVersions.robolectric}"
    const val mockk = "io.mockk:mockk:${TestVersions.mockk}"
}

object Dependencies {
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${DesignVersions.constraintLayoutVersion}"

    const val roomRunTime = "androidx.room:room-runtime:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"

    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    const val koin = "io.insert-koin:koin-core:${Versions.koin}"
    const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"

    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"

    const val lifeCycleExtensions =
        "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensions}"
    const val lifeCycleKapt = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycleKtx}"
    const val lifeCycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleKtx}"
    const val lifeCycleViewModel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleKtx}"

    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val cardView = "androidx.cardview:cardview:${Versions.cardView}"
    const val swipeRefreshLayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"
}
