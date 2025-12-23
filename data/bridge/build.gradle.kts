plugins {
    id("githubpopular.android.library")
    alias(libs.plugins.ksp.plugin)
}

android {
    namespace = "com.caioluis.githubpopular.data.bridge"

    defaultConfig {
        buildFeatures.buildConfig = true
        buildConfigField("String", "API_BASE_URL", "\"https://api.github.com/\"")
    }

    viewBinding {
        enable = true
    }
}

dependencies {
    api(project(":domain:bridge"))
    runtimeOnly(libs.room.runtime)

    implementation(libs.square.retrofit)
    implementation(libs.square.moshi)
    implementation(libs.room.common)

    ksp(libs.square.moshi.codegen)
    ksp(libs.room.compiler)

    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.coroutines.test)
}
