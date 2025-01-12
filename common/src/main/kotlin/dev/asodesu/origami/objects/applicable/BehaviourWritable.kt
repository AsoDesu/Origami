package dev.asodesu.origami.objects.applicable

import dev.asodesu.origami.behaviours.Behaviour
import dev.asodesu.origami.behaviours.factory.BehaviourFactory
import kotlin.reflect.KClass

interface BehaviourWritable : BehaviourApplicable {
    fun add(behaviour: Behaviour)
    fun <T : Behaviour> add(clazz: KClass<T>) = BehaviourFactory.construct(clazz).also { add(it) }
    fun <T : Behaviour> add(provider: () -> T) = provider().also { add(it) }
    operator fun plusAssign(behaviour: Behaviour) = add(behaviour)

    fun <T : Behaviour> getOrAdd(clazz: KClass<T>): T {
        getOrNull(clazz)?.let { return it }
        return add(clazz)
    }
    fun <T : Behaviour> getOrAdd(clazz: KClass<T>, provider: () -> T): T {
        getOrNull(clazz)?.let { return it }
        return add(provider)
    }

    fun <T : Behaviour> replace(clazz: KClass<T>): T {
        if (contains(clazz)) remove(clazz)
        return add(clazz)
    }
    fun replace(behaviour: Behaviour) {
        if (contains(behaviour::class)) remove(behaviour::class)
        add(behaviour)
    }
    fun <T : Behaviour> replace(clazz: KClass<T>, provider: () -> T): T {
        if (contains(clazz)) remove(clazz)
        return add(provider)
    }
}