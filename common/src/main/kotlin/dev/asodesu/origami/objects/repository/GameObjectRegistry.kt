package dev.asodesu.origami.objects.repository

import dev.asodesu.origami.objects.GameObject

open class GameObjectRegistry : GameObjectNode {
    val children = mutableListOf<GameObject>()

    override fun addChild(childObject: GameObject) {
        require(childObject.parent == null) { "provided gameObject is already assigned to a parent" }
        require(childObject != this) { "cannot add a gameObject as a child of itself" }
        children += childObject
        childObject.parent = this
    }

    override fun removeChild(childObject: GameObject) {
        require(childObject.parent == this) { "provided gameObject is not a child of this gameObject" }
        children -= childObject
        childObject.parent = this
    }
}