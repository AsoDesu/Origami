package dev.asodesu.origami.engine

import dev.asodesu.origami.engine.wiring.events.EventFilter
import org.bukkit.event.Event
import org.bukkit.event.Listener
import kotlin.reflect.KClass
import kotlin.time.Duration

/**
 * A Behaviour is a discrete component which adds custom logic to whatever
 * it is applied to.
 */
abstract class Behaviour : EventFilter, Listener {
    lateinit var gameObject: BehaviourApplicable

    protected fun checkCooldown(key: String, time: Duration) = gameObject.cooldowns.checkCooldown(key, time)
    protected fun consumeCooldown(key: String, time: Duration) = gameObject.cooldowns.consumeCooldown(key, time)

    protected val behaviours get() = gameObject.behaviours
    protected fun <T : Behaviour> get(clazz: KClass<T>) = gameObject.get(clazz)
    protected fun <T : Behaviour> getOrAdd(clazz: KClass<T>, creator: BehaviourCreator<T>?) = gameObject.getOrAdd(clazz, creator)
    protected fun <T : Behaviour> has(clazz: KClass<T>) = gameObject.has(clazz)
    protected fun <T : Behaviour> replace(clazz: KClass<T>, instance: T?) = gameObject.replace(clazz, instance)
    protected fun <T : Behaviour> add(clazz: KClass<T>, instance: T?) = gameObject.add(clazz, instance)
    protected fun <T : Behaviour> remove(clazz: KClass<T>) = gameObject.remove(clazz)

    override fun filter(event: Event): Boolean = true
}