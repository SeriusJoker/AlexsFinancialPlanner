plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    // Add SQLite JDBC dependency
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre") // Update the version if needed
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(22)) // Assuming you have Java 22 installed
    }
}

application {
    // Define the main class for the application.
    mainClass.set("org.example.App")
}

tasks.test {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
tasks.named<JavaExec>("run") {
    standardInput = System.`in`  // Set System.in as the standard input for interactive CLI
}