package dev.asodesu.origami.objects.applicable

import dev.asodesu.origami.behaviours.Behaviour
import kotlin.reflect.KClass

abstract class BehaviourContainer : BehaviourApplicable {
    protected val behaviourSet = mutableSetOf<Behaviour>()

    open fun behaviourParent(): BehaviourApplicable? = null

    override fun contains(clazz: KClass<out Behaviour>) = behaviourSet.any { clazz.isInstance(it) }

    override fun <T : Behaviour> getOrNull(clazz: KClass<T>) =
        behaviourSet.find { clazz.isInstance(it) } as? T ?: behaviourParent()?.getOrNull(clazz)

    override fun <T : Behaviour> getAll(clazz: KClass<T>) = behaviourSet.filter { clazz.isInstance(it) } as List<T>
    override fun getAll() = behaviourSet.toList()

    override fun remove(behaviour: Behaviour) {
        behaviourSet.remove(behaviour)
        internalRemove(behaviour)
    }
    override fun removeIf(predicate: (Behaviour) -> Boolean) {
        behaviourSet.removeIf remove@{
            if (predicate(it)) {
                internalRemove(it)
                return@remove true
            }
            return@remove false
        }
    }
    private fun internalRemove(behaviour: Behaviour) {
        behaviour.remove()
    }
}