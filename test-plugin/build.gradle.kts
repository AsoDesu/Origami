group = "dev.asodesu.origami.testplugin"

plugins {
    alias(libs.plugins.run.paper)
    alias(libs.plugins.shadow)
    alias(libs.plugins.pluginyml)
}

dependencies {
    compileOnly(libs.paper.api)

    implementation(project(":engine"))
    implementation(project(":utils:common"))
    implementation(project(":utils:commands"))
    implementation(project(":utils:designs"))
}

bukkit {
    name = "OrigamiTest"
    version = project.version.toString()
    description = "A plugin for testing the capabilities of Origami"
    website = "https://github.com/AsoDesu/Origami"
    author = "AsoDesu_"

    main = "dev.asodesu.origami.testplugin.OrigamiTestPlugin"
    apiVersion = "1.21"
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveClassifier = ""
    }
    jar {
        enabled = false
    }
}