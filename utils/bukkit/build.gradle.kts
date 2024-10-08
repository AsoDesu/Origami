group = "dev.asodesu.origami.utilities"

dependencies {
    implementation(project(":utils:common")) { isTransitive = true }
    implementation(project(":utils:designs")) { isTransitive = true }
    compileOnly(libs.paper.api)
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