package dev.asodesu.origami.scene

import dev.asodesu.origami.behaviours.Behaviour
import dev.asodesu.origami.objects.GameObject
import dev.asodesu.origami.objects.applicable.BehaviourContainer
import dev.asodesu.origami.objects.applicable.BehaviourWritable
import dev.asodesu.origami.objects.repository.GameObjectNode
import dev.asodesu.origami.util.ANSI_BLACK_BRIGHT
import dev.asodesu.origami.util.ANSI_GREEN_BRIGHT
import dev.asodesu.origami.util.ANSI_RESET
import dev.asodesu.origami.util.Disposable

abstract class Scene : BehaviourContainer(), BehaviourWritable, GameObjectNode, Disposable {
    var parent: Scene = GlobalScene
        private set
    private val childrenSet = mutableSetOf<Scene>()
    val children get() = childrenSet.toList()

    override fun behaviourParent() = parent
    final override fun scene(value: Scene) = this
    final override fun add(behaviour: Behaviour) {
        behaviour.apply(this, this)
        behaviourSet.add(behaviour)
    }

    fun addChild(childScene: Scene) {
        require(childScene.parent == GlobalScene) { "provided scene is already assigned to a parent" }
        require(childScene != this) { "cannot add a scene as a child of itself" }
        childrenSet += childScene
        childScene.parent = this
    }

    fun removeChild(childScene: Scene) {
        require(childScene.parent == this) { "provided scene is not a child of this scene" }
        childrenSet -= childScene
        childScene.parent = GlobalScene
    }

    open fun isChildOf(parentScene: Scene): Boolean {
        if (this.parent == parentScene) return true
        return this.parent.isChildOf(parentScene)
    }

    private val gameObjectSet = mutableListOf<GameObject>()
    val gameObjects get() = childrenSet.toList()

    override fun addChild(childObject: GameObject) {
        require(childObject.parent == null) { "provided gameObject is already assigned to a parent" }
        require(childObject != this) { "cannot add a gameObject as a child of itself" }
        gameObjectSet += childObject
        childObject.scene = this
    }

    override fun removeChild(childObject: GameObject) {
        require(childObject.parent == this) { "provided gameObject is not a child of this gameObject" }
        gameObjectSet -= childObject
        childObject.scene = GlobalScene
    }

    fun debugInfo(): String = buildString {
        appendLine("${ANSI_GREEN_BRIGHT}\uD83D\uDDE1 $name $ANSI_RESET")

        append(appliedBehavioursDebug())
        if (gameObjectSet.isNotEmpty()) {
            if (behaviourSet.isNotEmpty()) appendLine("${ANSI_BLACK_BRIGHT}|${ANSI_RESET}")
            gameObjectSet.forEach {
                appendLine(it.debugInfo().prependIndent("${ANSI_BLACK_BRIGHT}|${ANSI_RESET}  "))
            }
        }

        if (childrenSet.isNotEmpty()) {
            if (gameObjectSet.isNotEmpty()) appendLine("${ANSI_BLACK_BRIGHT}|${ANSI_RESET}")
            childrenSet.forEach {
                appendLine(it.debugInfo().prependIndent("${ANSI_BLACK_BRIGHT}|${ANSI_RESET}  "))
            }
        }
    }.trimEnd()

    override fun dispose() {
        children.toList().forEach { it.dispose() }
        gameObjects.toList().forEach { removeChild(it) }
        parent.removeChild(this)
    }
}