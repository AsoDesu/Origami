package dev.asodesu.origami.objects.builder

import dev.asodesu.origami.objects.GameObject
import dev.asodesu.origami.objects.GameObjectImpl
import dev.asodesu.origami.objects.ScopedGameObject
import dev.asodesu.origami.objects.repository.GameObjectNode
import dev.asodesu.origami.scene.GlobalScene
import dev.asodesu.origami.scene.Scene

fun GameObject(builder: GameObjectBuilder.() -> Unit): GameObject {
    return GameObjectBuilder().also(builder).build()
}

class GameObjectBuilder {
    private val gameObject = GameObjectImpl()

    fun name(string: String) {
        gameObject.name = string
    }

    fun extends(parent: GameObjectNode) {
        parent.addChild(gameObject)
    }

    fun behaviours(func: ScopedGameObject.() -> Unit) {
        gameObject.scene(GlobalScene, func)
    }

    fun behaviours(scene: Scene, func: ScopedGameObject.() -> Unit) {
        gameObject.scene(scene, func)
    }

    fun build() = gameObject
}