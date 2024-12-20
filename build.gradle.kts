
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.plugin.kotlinToolingVersion

plugins {
    kotlin("jvm") version "2.1.0"
//    kotlin("kapt") version "2.1.0"
//    kotlin("plugin.allopen") version "2.1.0"
    application
    id("org.graalvm.buildtools.native") version "0.10.4"
    id("me.champeau.jmh") version "0.7.2"
}

kotlin {
    jvmToolchain(23)
}

dependencies {
    implementation(libs.kotlinxCoroutines)

    implementation(platform("io.arrow-kt:arrow-stack:${libs.versions.arrow.get()}"))
    implementation("io.arrow-kt:arrow-core")
    implementation("io.arrow-kt:arrow-fx-coroutines")

    // implementation("org.choco-solver:choco-solver:4.10.7")
//    implementation("com.google.ortools:ortools-java:9.11.4210")
    implementation(libs.apacheCommons.collections4)

    implementation(libs.javaDiffUtils)

    implementation(libs.logback.classic)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.register<JavaExec>("benchmarkJvm") {
    group = "benchmark"
    description = "Runs the benchmark on JVM"

    dependsOn("assemble")

    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("aoc.measure.Aoc2024Kt")
//    jvmArgs = listOf("-server", "-Xms2g", "-Xmx2g")
    jvmArgs = listOf("-server", "-Xms2g", "-Xmx2g", "-XX:+UseSerialGC")

    outputs.upToDateWhen { false }

    doLast {
        val jdk = javaLauncher.get().metadata

        println()
        println("Used JDK: ${jdk.jvmVersion} ${jdk.vendor}")
        println("Used Kotlin version: ${kotlinToolingVersion}")
    }
}

tasks.register("benchmarkJmh") {
    group = "benchmark"
    description = "Runs the JMH benchmark"

    dependsOn("jmh")

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

//    jvmArgs = listOf("-server", "-Xss515m")
}

graalvmNative {
    toolchainDetection.set(true)

    binaries {
        named("main") {
            imageName.set("aoc")
            mainClass.set("aoc.measure.Aoc2024Kt")
            buildArgs.addAll(listOf("-O4", "-march=native", "--link-at-build-time"))

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

jmh { // https://github.com/melix/jmh-gradle-plugin
    jmhVersion = "1.37" // Specifies JMH version
    resultsFile = layout.projectDirectory.file("src/jmh/jmh_results.txt")
    verbosity = "EXTRA"
    benchmarkMode = listOf("avgt")

    fork = 1
    warmupForks = 1
}
