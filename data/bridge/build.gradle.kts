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

    testOptions {
        unitTests {
            isReturnDefaultValues = true
            all {
                it.useJUnitPlatform()
            }
        }
    }
}

dependencies {
    implementation(project(":domain:bridge"))
    runtimeOnly(libs.room.runtime)

    implementation(libs.square.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.room.common)

    ksp(libs.room.compiler)

    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}
