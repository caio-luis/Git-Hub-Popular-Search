plugins {
    id("githubpopular.jvm.library")
    id("githubpopular.jacoco")
    alias(libs.plugins.ksp.plugin)
}

dependencies {
    implementation(project(":domain:bridge"))

    implementation(libs.kotlinx.coroutines)
    implementation(libs.hilt.core)
    implementation(libs.androidx.paging.common)

    ksp(libs.hilt.core.compiler)

    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.dsl)
}
