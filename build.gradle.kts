
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin
    kotlin("jvm") version "2.1.0"
}

kotlin {
    // Use a specific Java version to make it easier to work in different environments.
    jvmToolchain(23)
}

dependencies {
    implementation(libs.apacheCommons.text)
    implementation(libs.javaDiffUtils)
    implementation(libs.guava)
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
