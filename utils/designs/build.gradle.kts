group = "dev.asodesu.origami.utilities"

dependencies {
    compileOnly(libs.kyori.adventure)
    compileOnly(libs.kyori.minimessage)
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