import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport

class JacocoConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("jacoco")

            extensions.configure(JacocoPluginExtension::class.java) {
                toolVersion = "0.8.13"
            }

            tasks.withType<Test>().configureEach {
                extensions.configure(JacocoTaskExtension::class.java) {
                    isIncludeNoLocationClasses = true
                    excludes = mutableListOf("jdk.internal.*")
                }
            }

            val classExcludes = listOf(
                "**/R.class",
                "**/R$*.class",
                "**/BuildConfig.*",
                "**/Manifest*.*",
                "**/*Test*.*",
                "**/*\$Lambda\$*.*",
                "**/*Companion*.*",
                "**/*Preview*.*",
                "**/ComposableSingletons*.*",
                "**/*_Impl*.*",
                "**/*\$DefaultImpls*.*",
                "**/*Module*.*",
                "**/*Factory*.*",
                "**/*_Factory*.*",
                "**/*_HiltModules*.*",
                "**/Dagger*.*",
                "**/Hilt_*.*",
                "**/*_ComponentTreeDeps*.*",
                "**/hilt_aggregated_deps/**",
                "**/dagger/hilt/**",
                "**/di/**",
                "**/model/**",
                "**/entity/**",
                "**/navigation/**",
                "**/ui/**",
                "**/theme/**",
                "**/activity/**",
                "**/service/**",
                "**/*Dao*.*",
                "**/*Application*.*",
                "android/**/*.*",
            )

            val configureReport: JacocoReport.() -> Unit = {
                dependsOn(tasks.withType<Test>())

                reports {
                    xml.required.set(true)
                    html.required.set(true)
                    csv.required.set(false)
                }

                val androidClasses = fileTree(layout.buildDirectory.get().asFile) {
                    include("intermediates/classes/debug/transformDebugClassesWithAsm/dirs/**/*.class")
                    exclude(classExcludes)
                }
                val jvmKotlinClasses = fileTree("${layout.buildDirectory.get()}/classes/kotlin/main") {
                    exclude(classExcludes)
                }
                val jvmJavaClasses = fileTree("${layout.buildDirectory.get()}/classes/java/main") {
                    exclude(classExcludes)
                }

                classDirectories.setFrom(files(androidClasses, jvmKotlinClasses, jvmJavaClasses))

                sourceDirectories.setFrom(
                    files(
                        "src/main/java",
                        "src/main/kotlin",
                    ),
                )

                val androidExec = fileTree(layout.buildDirectory.get().asFile) {
                    include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
                    include("jacoco/testDebugUnitTest.exec")
                }
                val jvmExec = fileTree(layout.buildDirectory.get().asFile) {
                    include("jacoco/test.exec")
                    include("jacoco/*.exec")
                }

                executionData.setFrom(files(androidExec, jvmExec))
            }

            if (tasks.findByName("jacocoTestReport") == null) {
                tasks.register<JacocoReport>("jacocoTestReport", configureReport)
            } else {
                tasks.named<JacocoReport>("jacocoTestReport").configure(configureReport)
            }

            val reportTask = tasks.named<JacocoReport>("jacocoTestReport")

            val configureVerification: JacocoCoverageVerification.() -> Unit = {
                dependsOn(reportTask)

                violationRules {
                    rule {
                        limit {
                            minimum = "0.90".toBigDecimal()
                        }
                    }
                }

                classDirectories.setFrom(reportTask.map { it.classDirectories })
                sourceDirectories.setFrom(reportTask.map { it.sourceDirectories })
                executionData.setFrom(reportTask.map { it.executionData })
            }

            if (tasks.findByName("jacocoTestCoverageVerification") == null) {
                tasks.register<JacocoCoverageVerification>(
                    "jacocoTestCoverageVerification",
                    configureVerification,
                )
            } else {
                tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification")
                    .configure(configureVerification)
            }
        }
    }
}









