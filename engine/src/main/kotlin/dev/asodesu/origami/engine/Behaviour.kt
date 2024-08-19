package dev.asodesu.origami.engine

import dev.asodesu.origami.engine.scopes.Scope
import dev.asodesu.origami.engine.wiring.events.EventFilter
import org.bukkit.event.Event
import org.bukkit.event.Listener

/**
 * A Behaviour is a discrete component which adds custom logic to whatever
 * it is applied to.
 */
abstract class Behaviour : EventFilter, Destroyable, Listener {
    internal lateinit var internalGameObject: BehaviourApplicable
    internal var internalScope: Scope = Scope.global

    protected val gameObject get() = internalGameObject
    protected val scope get() = internalScope

    protected inline fun <reified T : Behaviour> getOrNull() = gameObject.getOrNull(T::class)
    protected inline fun <reified T : Behaviour> get() = gameObject.get(T::class)
    protected inline fun <reified T : Behaviour> getOrAdd(scope: Scope? = this.scope, noinline creator: BehaviourCreator<T>? = null) = gameObject.getOrAdd(T::class, scope, creator)
    protected inline fun <reified T : Behaviour> has() = gameObject.has(T::class)
    protected inline fun <reified T : Behaviour> replace(instance: T? = null, scope: Scope? = this.scope) = gameObject.replace(T::class, instance, scope)
    protected inline fun <reified T : Behaviour> add(instance: T? = null, scope: Scope? = this.scope) = gameObject.add(T::class, instance, scope)
    protected inline fun <reified T : Behaviour> remove() = gameObject.remove(T::class)

    override fun filter(event: Event): Boolean = true

    override fun destroy() {
        gameObject.remove(this::class)
    }
}