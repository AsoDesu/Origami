package dev.asodesu.origami.engine.scene

import dev.asodesu.origami.engine.Behaviour
import dev.asodesu.origami.engine.Destroyable
import dev.asodesu.origami.engine.impl.BehaviourContainer
import dev.asodesu.origami.engine.scopes.CurrentScopeContext
import dev.asodesu.origami.engine.scopes.Scope
import dev.asodesu.origami.engine.scopes.scope
import dev.asodesu.origami.engine.wiring.events.EventFilter
import dev.asodesu.origami.engine.wiring.events.FilteredEvents
import dev.asodesu.origami.utilities.bukkit.unregister
import dev.asodesu.origami.utilities.randomString
import org.bukkit.event.Listener

abstract class Scene(
    override val id: String = "~${randomString(8)}"
) : Scope, EventFilter, BehaviourContainer() {
    protected val contents = mutableListOf<Destroyable>()
    protected val scopedBehaviours = mutableListOf<Behaviour>()

    open fun init() {
        if (this is Listener) {
            FilteredEvents.locateAndRegister(this, this)
        }

        scope(this) {
            setupComponents()
        }
    }

    protected abstract fun setupComponents()

    override fun addDestroyable(destroyable: Destroyable) {
        if (destroyable is Behaviour) scopedBehaviours += destroyable
        else contents.add(destroyable)
    }
    override fun removeDestroyable(destroyable: Destroyable) {
        if (destroyable is Behaviour) scopedBehaviours -= destroyable
        else contents.remove(destroyable)
    }
    override fun destroy() {
        contents.toList().forEach { it.destroy() }
        contents.clear()

        scopedBehaviours.toList().forEach { it.discard() }
        scopedBehaviours.clear()

        if (this is Listener) this.unregister()
    }
    override fun use(func: () -> Unit) {
        CurrentScopeContext.push(this)
        func()
        CurrentScopeContext.pop()
    }
}