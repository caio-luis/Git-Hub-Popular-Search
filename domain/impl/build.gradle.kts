plugins {
    id("githubpopular.jvm.library")
    alias(libs.plugins.ksp.plugin)
}

dependencies {
    api(project(":domain:bridge"))

    implementation(libs.kotlinx.coroutines)
    implementation(libs.hilt.core)

    ksp(libs.hilt.core.compiler)

    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.dsl)
}
