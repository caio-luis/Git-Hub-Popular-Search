plugins {
    id("githubpopular.android.library")
    id("githubpopular.jacoco")
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.caioluis.githubpopular.data.impl"

    defaultConfig {
        buildFeatures.buildConfig = true
        buildConfigField("String", "API_BASE_URL", "\"https://api.github.com/\"")
    }
}

dependencies {
    implementation(project(":domain:bridge"))
    implementation(project(":data:bridge"))
    implementation(project(":core:common"))

    runtimeOnly(libs.room.runtime)

    implementation(libs.kotlinx.coroutines)
    implementation(libs.hilt.android)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    implementation(libs.square.retrofit)
    implementation(libs.square.okhttp)
    implementation(libs.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.square.okhttp.logging.interceptor)
    implementation(libs.room.common)
    implementation(libs.androidx.sqlite)
    implementation(libs.androidx.paging.common)
    implementation(libs.timber)

    ksp(libs.room.compiler)
    ksp(libs.hilt.compiler)

    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.junit.ktx)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.robolectric)

    androidTestImplementation(libs.android.test.runner)
    androidTestImplementation(libs.androidx.junit.ktx)
}
