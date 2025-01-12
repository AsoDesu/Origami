package dev.asodesu.origami.objects

import dev.asodesu.origami.behaviours.Behaviour
import dev.asodesu.origami.scene.Scene

class ScopedGameObjectImpl(
    private val gameObject: GameObjectImpl,
    private val behaviours: MutableCollection<Behaviour>,
    private val sceneScope: Scene
) : ScopedGameObject, GameObject by gameObject {
    override fun add(behaviour: Behaviour) {
        behaviour.apply(gameObject, sceneScope)
        behaviours.add(behaviour)
    }
}