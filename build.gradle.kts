import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import xyz.jpenilla.runpaper.task.RunServer

val origamiVersion = findProperty("origami.version")?.toString() ?: "unmarked"

plugins {
    kotlin("jvm") version "2.0.10" apply false
    alias(libs.plugins.run.paper) apply false
    `maven-publish`
}

group = "dev.asodesu.origami"

allprojects {
    version = origamiVersion

    repositories {
        mavenCentral()
        maven("https://papermc.io/repo/repository/maven-public/")
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "maven-publish")
    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    tasks.withType<RunServer> {
        minecraftVersion("1.21.1")
        systemProperty("paper.playerconnection.keepalive", 36000)
        systemProperty("paper.disablePluginRemapping", true)
        systemProperty("paper.disableOldApiSupport", true)
        if (project.hasProperty("hotswapping")) {
            jvmArgs("-XX:+AllowEnhancedClassRedefinition")
        }
    }
}