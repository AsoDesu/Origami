package dev.asodesu.origami.objects

import dev.asodesu.origami.objects.applicable.BehaviourContainer
import dev.asodesu.origami.objects.repository.GameObjectNode
import dev.asodesu.origami.objects.repository.GameObjectRegistry
import dev.asodesu.origami.scene.GlobalScene
import dev.asodesu.origami.scene.Scene
import dev.asodesu.origami.util.randomString

class GameObjectImpl : GameObject, BehaviourContainer() {
    override var name: String = "gameObject_" + randomString(8)

    override var scene: Scene = GlobalScene

    override var parent: GameObjectNode? = null
    override val children = mutableListOf<GameObject>()

    override fun addChild(childObject: GameObject) {
        require(childObject.parent == null) { "provided gameObject is already assigned to a parent" }
        require(childObject != this) { "cannot add a gameObject as a child of itself" }
        children += childObject
        childObject.parent = this
    }

    override fun removeChild(childObject: GameObject) {
        require(childObject.parent == this) { "provided gameObject is not a child of this gameObject" }
        children -= childObject
        childObject.parent = null
    }

    override fun behaviourParent() = parent as? GameObject
    override fun scene(value: Scene) = ScopedGameObjectImpl(this, behaviourSet, value)

    override fun dispose() {
        // children are disposed first because they may still need access to our
        //  behaviours, our scene or our parent to dispose properly
        children.toList().forEach { it.dispose() }
        parent?.removeChild(this)
        this.removeIf { true } // remove all behaviours
        scene.removeChild(this)
        scene = GlobalScene
        parent = null
    }
}