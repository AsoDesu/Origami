package dev.asodesu.origami.engine.scopes

import dev.asodesu.origami.engine.Destroyable

class GlobalScope : Scope {
    override val id = "~global"
    private val contents = mutableListOf<Destroyable>()

    override fun addDestroyable(destroyable: Destroyable) {
        contents.add(destroyable)
    }
    override fun removeDestroyable(destroyable: Destroyable) {
        contents.remove(destroyable)
    }

    override fun destroy() {
        contents.forEach { it.destroy() }
        contents.clear()
    }
}