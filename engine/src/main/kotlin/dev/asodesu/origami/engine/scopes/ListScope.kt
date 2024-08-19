package dev.asodesu.origami.engine.scopes

import dev.asodesu.origami.engine.Destroyable

open class ListScope(override val id: String) : Scope {
    private val contents = mutableListOf<Destroyable>()

    override fun add(destroyable: Destroyable) = contents.add(destroyable)
    override fun remove(destroyable: Destroyable) = contents.remove(destroyable)

    override fun destroy() {
        contents.forEach { it.destroy() }
        contents.clear()
    }
}