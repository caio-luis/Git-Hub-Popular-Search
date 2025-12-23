// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp.plugin) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.dependency.analysis)
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("**/build/**/*.kt")
            ktlint()
        }

        kotlinGradle {
            target("*.gradle.kts")
            ktlint()
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}

tasks.register("installGitHooks") {
    description = "Installs git hooks to run spotlessCheck before commit"
    group = "git hooks"

    doLast {
        val gitHooksDir = File(rootProject.rootDir, ".git/hooks")
        if (!gitHooksDir.exists()) return@doLast

        val preCommitFile = File(gitHooksDir, "pre-commit")

        val scriptContent = """
            #!/bin/sh
            echo "Running spotlessCheck..."
            ./gradlew spotlessCheck
        """.trimIndent()

        if (!preCommitFile.exists() || preCommitFile.readText().trim() != scriptContent.trim()) {
            preCommitFile.writeText(scriptContent)
            preCommitFile.setExecutable(true)
            println("Git pre-commit hook installed/updated.")
        }
    }
}

tasks.named("clean") {
    dependsOn("installGitHooks")
}

