group = "dev.asodesu.origami.engine"

dependencies {
    compileOnly(libs.paper.api)
    implementation(kotlin("reflect"))

    api(project(":utils:common")) { isTransitive = true }
    api(project(":utils:commands")) { isTransitive = true }
    api(project(":utils:designs")) { isTransitive = true }
    api(project(":utils:bukkit")) { isTransitive = true }
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
        }
    }
}