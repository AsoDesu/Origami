package dev.asodesu.origami.engine.scopes

import dev.asodesu.origami.engine.Destroyable

interface SceneScope : Destroyable {
    companion object {
        val global = GlobalScope()
    }

    val id: String

    fun addDestroyable(destroyable: Destroyable)
    fun removeDestroyable(destroyable: Destroyable)

    fun use(func: () -> Unit) {
        CurrentScopeContext.push(this)
        func()
        CurrentScopeContext.pop()
    }
}