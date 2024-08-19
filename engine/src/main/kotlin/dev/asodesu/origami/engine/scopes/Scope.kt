package dev.asodesu.origami.engine.scopes

import dev.asodesu.origami.engine.Destroyable

interface Scope : Destroyable {
    companion object {
        val global = ListScope("~global")
    }

    val id: String

    fun add(destroyable: Destroyable): Boolean
    fun remove(destroyable: Destroyable): Boolean

    fun use(func: () -> Unit) {
        CurrentScopeContext.push(this)
        func()
        CurrentScopeContext.pop()
    }
}