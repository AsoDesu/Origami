group = "dev.asodesu.origami.utilities"

dependencies {
    compileOnly(libs.paper.api)
    api(libs.cloud.core) { isTransitive = true }
    api(libs.cloud.paper) { isTransitive = true }
    api(libs.cloud.kotlin) { isTransitive = true }
    api(libs.cloud.mcextras) { isTransitive = true }
}