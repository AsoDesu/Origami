package dev.asodesu.origami.engine.impl

import dev.asodesu.origami.engine.Behaviour
import dev.asodesu.origami.engine.BehaviourApplicable
import dev.asodesu.origami.engine.BehaviourCreator
import dev.asodesu.origami.engine.debug.Debuggable
import dev.asodesu.origami.engine.error.BehaviourAlreadyAttachedException
import dev.asodesu.origami.engine.scopes.CurrentScopeContext
import dev.asodesu.origami.engine.scopes.Scope
import dev.asodesu.origami.engine.wiring.BehaviourWiring.wiring
import dev.asodesu.origami.utilities.bukkit.unregister
import kotlin.reflect.KClass

open class BehaviourContainer : BehaviourApplicable, Debuggable {
    protected val behaviourMap = mutableMapOf<Class<out Behaviour>, Behaviour>()
    override val behaviours get() = behaviourMap.values

    override fun <T : Behaviour> get(clazz: KClass<T>): T {
        return getOrNull(clazz)
            ?: throw IllegalStateException("Behaviour '${clazz.simpleName}' was not found in this container")
    }

    override fun <T : Behaviour> getOrNull(clazz: KClass<T>): T? {
        val instance = behaviourMap[clazz.java]
        return instance as? T
    }

    override fun <T : Behaviour> getOrAdd(clazz: KClass<T>, scope: Scope?, creator: BehaviourCreator<T>?): T {
        val get = getOrNull(clazz)
        if (get != null) return get
        return add(clazz, creator?.invoke(this), scope)
    }

    override fun <T : Behaviour> has(clazz: KClass<T>) = behaviourMap.containsKey(clazz.java)

    override fun <T : Behaviour> add(clazz: KClass<T>, instance: T?, scope: Scope?): T {
        if (has(clazz)) {
            throw BehaviourAlreadyAttachedException("Behaviour '${clazz.java.name}' is already attached to this container. " +
                    "Use BehaviourApplicable#replace to replace a behaviour")
        }

        val realScope = scope ?: CurrentScopeContext.peek()
        val behaviourToAdd = instance ?: BehaviourFactory.create(clazz, this)
        behaviourMap[clazz.java] = behaviourToAdd

        realScope.addDestroyable(behaviourToAdd)
        behaviourToAdd.internalGameObject = this
        behaviourToAdd.internalScope = realScope

        val wiring = behaviourToAdd.wiring
        wiring.registerEvents(behaviourToAdd)
        wiring.registerTickMethods(behaviourToAdd)
        wiring.postApply(behaviourToAdd)

        return behaviourToAdd
    }

    override fun <T : Behaviour> replace(clazz: KClass<T>, instance: T?, scope: Scope?): T {
        remove(clazz)
        return add(clazz, instance, scope)
    }

    override fun <T : Behaviour> remove(clazz: KClass<T>): T? {
        val removed = behaviourMap.remove(clazz.java) ?: return null
        destroyBehaviour(removed)
        return removed as? T
    }

    override fun destroyBehaviour(instance: Behaviour) {
        instance.internalScope.removeDestroyable(instance)
        instance.destroy()
        instance.unregister()
        instance.wiring.postRemove(instance)
    }

    override fun getDebugInfo() = buildString {
        appendLine("Behaviours: <origami>[${behaviours.size} applied]</origami>")
        behaviours.forEachIndexed { i, it ->
            appendLine(" - <origami>${it::class.simpleName}</origami>")
        }
    }
}