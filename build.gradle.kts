import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
}

allprojects {

    group = "com.messengerk"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {

    apply(plugin = "kotlin")

    dependencies {
        testImplementation(kotlin("test"))
        testImplementation("io.strikt:strikt-core:0.33.0")
    }

    tasks.test {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile>() {
        kotlinOptions.jvmTarget = "1.8"
    }
}



