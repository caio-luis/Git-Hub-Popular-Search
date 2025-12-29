plugins {
    id("githubpopular.android.library")
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.caioluis.githubpopular.data.bridge"

    defaultConfig {
        buildFeatures.buildConfig = true
        buildConfigField("String", "API_BASE_URL", "\"https://api.github.com/\"")
    }
}

dependencies {
    api(project(":domain:bridge"))
    runtimeOnly(libs.room.runtime)

    implementation(libs.square.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.room.common)

    ksp(libs.room.compiler)

    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.coroutines.test)
}
