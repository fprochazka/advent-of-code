
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm") version "2.1.0"
//    kotlin("kapt") version "2.1.0"
//    kotlin("plugin.allopen") version "2.1.0"
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
