
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm") version "2.1.0"
//    kotlin("kapt") version "2.1.0"
//    kotlin("plugin.allopen") version "2.1.0"
    application
    id("org.graalvm.buildtools.native") version "0.10.4"
}

kotlin {
    jvmToolchain(23)
}

dependencies {
    implementation(libs.kotlinxCoroutines)

    implementation(platform("io.arrow-kt:arrow-stack:${libs.versions.arrow.get()}"))
    implementation("io.arrow-kt:arrow-core")
    implementation("io.arrow-kt:arrow-fx-coroutines")

    implementation(libs.javaDiffUtils)
}

tasks.register<JavaExec>("benchmarkJvm") {
    group = "benchmark"
    description = "Runs the benchmark on JVM"

    dependsOn("assemble")
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("aoc.measure.Aoc2024Kt")

    outputs.upToDateWhen { false }
}

tasks.register("benchmarkNative") {
    group = "benchmark"
    description = "Runs the benchmark using native image build by GraalVM"

    dependsOn("nativeRun")

    outputs.upToDateWhen { false }
}

tasks.withType<Test>().configureEach {
    // Configure all test Gradle tasks to use JUnitPlatform.
    useJUnitPlatform()

    // Log information about all test results, not only the failed ones.
    testLogging {
        events(
            TestLogEvent.FAILED,
            TestLogEvent.PASSED,
            TestLogEvent.SKIPPED
        )
    }
}

graalvmNative {
    toolchainDetection.set(true)

    binaries {
        named("main") {
            imageName.set("my-app")
            mainClass.set("aoc.measure.Aoc2024Kt")
            buildArgs.add("-O4")

            javaLauncher.set(javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(23))
                vendor.set(JvmVendorSpec.matching("GraalVM Community"))
            })
        }
    }
    binaries.all {
        buildArgs.add("--verbose")
    }
}
