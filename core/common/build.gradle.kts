plugins {
    id("githubpopular.android.library")
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.caioluis.githubpopular.core.common"

    defaultConfig {
        buildFeatures.buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.annotation.jvm)
    implementation(libs.androidx.core.ktx)
    implementation(libs.square.retrofit)
    implementation(libs.square.okhttp.logging.interceptor)
    implementation(libs.square.okhttp)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.converter)
    implementation(libs.hilt.android)

    ksp(libs.hilt.compiler)

    testImplementation(libs.mockk)
    testImplementation(libs.junit)
}
