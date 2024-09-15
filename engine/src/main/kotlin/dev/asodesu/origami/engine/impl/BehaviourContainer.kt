package dev.asodesu.origami.engine.impl

import dev.asodesu.origami.engine.Behaviour
import dev.asodesu.origami.engine.BehaviourApplicable
import dev.asodesu.origami.engine.BehaviourCreator
import dev.asodesu.origami.engine.debug.Debuggable
import dev.asodesu.origami.engine.scopes.CurrentScopeContext
import dev.asodesu.origami.engine.scopes.SceneScope
import dev.asodesu.origami.engine.wiring.BehaviourWiring.wiring
import dev.asodesu.origami.utilities.bukkit.unregister
import kotlin.reflect.KClass

open class BehaviourContainer : BehaviourApplicable, Debuggable {
    override val behaviours = mutableListOf<Behaviour>()

    override fun <T : Behaviour> get(clazz: KClass<T>): T {
        return getOrNull(clazz)
            ?: throw IllegalStateException("Behaviour '${clazz.simpleName}' was not found in this container")
    }

    override fun <T : Behaviour> getOrNull(clazz: KClass<T>): T? {
        return behaviours.find { clazz.java.isAssignableFrom(it::class.java) } as? T
    }

    override fun <T : Behaviour> getOrAdd(clazz: KClass<T>, scope: SceneScope?, creator: BehaviourCreator<T>?): T {
        val get = getOrNull(clazz)
        if (get != null) return get
        return add(clazz, creator?.invoke(this), scope)
    }

    override fun <T : Behaviour> has(clazz: KClass<T>) = getOrNull(clazz) != null

    override fun <T : Behaviour> add(clazz: KClass<T>, instance: T?, scope: SceneScope?): T {
        val realScope = scope ?: CurrentScopeContext.peek()
        val behaviourToAdd = instance ?: BehaviourFactory.create(clazz, this)
        behaviours += behaviourToAdd

        realScope.addDestroyable(behaviourToAdd)
        behaviourToAdd.internalGameObject = this
        behaviourToAdd.internalScope = realScope

        val wiring = behaviourToAdd.wiring
        wiring.registerEvents(behaviourToAdd)
        behaviourToAdd.internalTickTasks = wiring.registerTickMethods(behaviourToAdd)
        wiring.postApply(behaviourToAdd)

        return behaviourToAdd
    }

    override fun <T : Behaviour> replace(clazz: KClass<T>, instance: T?, scope: SceneScope?): T {
        removeAll(clazz)
        return add(clazz, instance, scope)
    }

    override fun <T : Behaviour> remove(instance: T): Boolean {
        if (!behaviours.remove(instance)) return false
        destroyBehaviour(instance)
        return true
    }

    override fun <T : Behaviour> removeFirst(clazz: KClass<T>): T? {
        val toRemove = behaviours.firstOrNull { clazz.java.isAssignableFrom(it::class.java) }
            ?: return null
        destroyBehaviour(toRemove)
        return toRemove as? T
    }

    override fun <T : Behaviour> removeAll(clazz: KClass<T>): List<T> {
        val removed = behaviours.toList().mapNotNull {
            if (clazz.java.isAssignableFrom(it::class.java)) {
                destroyBehaviour(it)
                it as? T
            } else null
        }
        return removed
    }

    override fun destroyBehaviour(instance: Behaviour) {
        instance.internalScope.removeDestroyable(instance)
        instance.destroy()
        instance.unregister()

        instance.internalTickTasks.forEach { it.cancel() }
        instance.internalTickTasks = emptyList()

        instance.wiring.postRemove(instance)
    }

    override fun getDebugInfo() = buildString {
        appendLine("Behaviours: <origami>[${behaviours.size} applied]</origami>")
        behaviours.forEachIndexed { i, it ->
            appendLine(" - <origami>${it::class.simpleName}</origami>")
        }
    }
}