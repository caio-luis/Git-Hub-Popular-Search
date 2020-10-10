/**
 * Created by Caio Luis (@caio.luis) on 03/10/20
 */

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
    const val  jUnit = "androidx.test.ext:junit:${TestVersions.androidxJunit}"
    const val  assertJ = "org.assertj:assertj-core:${TestVersions.assertJ}"
    const val  androidTestCore = "androidx.test:core:${TestVersions.androidTestArchCore}"
    const val  androidTestRunner = "androidx.test:runner:${TestVersions.androidTextRunner}"
    const val  androidTestRules = "androidx.test:rules:${TestVersions.androidTextRules}"
    const val  espresso = "androidx.test.espresso:espresso-core:${TestVersions.espresso}"
    const val  coreTesting = "androidx.arch.core:core-testing:${TestVersions.coreTesting}"
}

object Dependencies {
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${DesignVersions.constraintLayoutVersion}"

    const val  roomRunTime = "androidx.room:room-runtime:${Versions.room}"
    const val  roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val  roomCompiler = "androidx.room:room-compiler:${Versions.room}"

    const val  retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val  gson = "com.google.code.gson:gson:${Versions.gson}"
}


