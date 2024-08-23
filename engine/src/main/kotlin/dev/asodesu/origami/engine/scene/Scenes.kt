package dev.asodesu.origami.engine.scene

object Scenes {
    val map = mutableMapOf<String, Scene>()

    fun register(scene: Scene) {
        val sceneId = scene.id
        if (map.containsKey(sceneId))
            throw IllegalStateException("Scene already registered with id $sceneId")
        scene.verifyRegistration()

        map[sceneId] = scene
        scene.init()
    }

    fun unregister(id: String) {
        val scene = map[id] ?: return
        unregister(scene)
    }

    fun unregister(scene: Scene) {
        scene.destroy()
        map.remove(scene.id)
    }
}