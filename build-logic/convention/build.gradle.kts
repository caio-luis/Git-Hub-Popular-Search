plugins {
    `kotlin-dsl`
}

group = "com.caioluis.githubpopular.buildlogic"

dependencies {
    compileOnly(libs.android.gradle)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "githubpopular.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidApplication") {
            id = "githubpopular.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("jvmLibrary") {
            id = "githubpopular.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}
