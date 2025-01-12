package dev.asodesu.origami.scene

fun <T : Scene> T.asChildOf(parent: Scene): T {
    parent.addChild(this)
    return this
}