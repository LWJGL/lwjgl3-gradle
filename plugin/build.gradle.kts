import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    `java-gradle-plugin`

    // Apply the Kotlin JVM plugin to add support for Kotlin.
    kotlin("jvm") version embeddedKotlinVersion

    `kotlin-dsl`

    id("com.gradle.plugin-publish") version "0.14.0"

    `maven-publish`
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform(kotlin("bom", embeddedKotlinVersion)))

    // Use the Kotlin JDK 8 standard library.
    implementation(kotlin("stdlib-jdk8"))

    // Use the Kotlin test library.
    testImplementation(kotlin("test"))

    // Use the Kotlin JUnit integration.
    testImplementation(kotlin("test-junit"))
}

group = "org.lwjgl"
version = "0.0.16"

publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = rootProject.name
    }
}

gradlePlugin {
    // Define the plugin
    plugins.create("lwjgl") {
        id = "org.lwjgl.plugin"
        displayName = "Lwjgl Gradle util"
        description = "Easier Lwjgl dependency management"
        implementationClass = "org.lwjgl.LwjglPlugin"
    }
}

pluginBundle {
    website = "https://github.com/LWJGL/lwjgl3-gradle"
    vcsUrl = "https://github.com/LWJGL/lwjgl3-gradle"
    tags = listOf("lwjgl", "gradle", "dependency", "easy", "management")
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}


// Add a source set for the functional test suite
//val functionalTestSourceSet = sourceSets.create("functionalTest") {
//}
//
//gradlePlugin.testSourceSets(functionalTestSourceSet)
//configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])
//
//// Add a task to run the functional tests
//val functionalTest by tasks.registering(Test::class) {
//    testClassesDirs = functionalTestSourceSet.output.classesDirs
//    classpath = functionalTestSourceSet.runtimeClasspath
//}
//
//tasks.check {
//    // Run the functional tests as part of `check`
//    dependsOn(functionalTest)
//}
