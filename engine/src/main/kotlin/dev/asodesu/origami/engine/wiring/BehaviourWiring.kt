package dev.asodesu.origami.engine.wiring

import dev.asodesu.origami.engine.Behaviour
import dev.asodesu.origami.engine.wiring.annotations.PostApply
import dev.asodesu.origami.engine.wiring.annotations.PostRemove
import dev.asodesu.origami.engine.wiring.events.EventFilter
import dev.asodesu.origami.engine.wiring.events.EventMethods
import dev.asodesu.origami.engine.wiring.events.FilteredEvents
import dev.asodesu.origami.engine.wiring.events.ticks.TickEvents
import dev.asodesu.origami.engine.wiring.events.ticks.TickFilter
import dev.asodesu.origami.engine.wiring.events.ticks.TickMethods
import dev.asodesu.origami.utilities.allMethods
import dev.asodesu.origami.utilities.isDevEnvironment
import java.lang.reflect.Method

internal object BehaviourWiring {
    private val cache = mutableMapOf<Class<out Behaviour>, BehaviourMeta>()

    fun get(clazz: Class<out Behaviour>): BehaviourMeta {
        var meta = cache[clazz]
        // generate new meta if we don't have any cached, or the class has been hotswapped.
        if (isDevEnvironment || meta == null) {
            meta = BehaviourMeta(clazz)
            cache[clazz] = meta
        }
        return meta
    }

    val Behaviour.wiring get() = get(this::class.java)

    class BehaviourMeta(clazz: Class<out Behaviour>) {
        private val applyFunctions = mutableListOf<Method>()
        private val removeFunctions = mutableListOf<Method>()
        private val tickMethods: TickMethods
        private val eventMethods: EventMethods

        init {
            val methods = clazz.allMethods
            methods.forEach(::checkMethod)

            tickMethods = TickEvents.locate(methods)
            eventMethods = FilteredEvents.locate(clazz, methods)
        }

        fun postApply(behaviour: Behaviour) = applyFunctions.forEach { it.invoke(behaviour) }
        fun postRemove(behaviour: Behaviour) = removeFunctions.forEach { it.invoke(behaviour) }
        fun registerTickMethods(behaviour: Behaviour) {
            if (tickMethods.isEmpty()) return
            val filters = listOfNotNull(behaviour.internalScope as? TickFilter, behaviour as? TickFilter).toTypedArray()
            val filter = when {
                filters.size > 1 -> TickFilter.all(*filters)
                filters.size == 1 -> filters.first()
                else -> null
            }
            TickEvents.register(behaviour, filter, tickMethods)
        }
        fun registerEvents(behaviour: Behaviour) {
            if (eventMethods.isEmpty()) return
            val filter = if (behaviour.internalScope is EventFilter)
                EventFilter.all(behaviour.internalScope as EventFilter, behaviour)
            else {
                behaviour
            }
            FilteredEvents.register(behaviour, filter, eventMethods)
        }

        private fun checkMethod(method: Method) {
            // check for @PostApply methods, or the postApply() function
            if (method.isAnnotationPresent(PostApply::class.java)) checkPostApplyFunction(method)
            else if (applyFunctions.isEmpty() && method.name == "postApply") checkPostApplyFunction(method)

            // check for @PostRemove methods, or the postRemove() function
            if (method.isAnnotationPresent(PostRemove::class.java)) checkPostRemoveFunction(method)
            else if (removeFunctions.isEmpty() && method.name == "postRemove") checkPostRemoveFunction(method)
        }

        private fun checkPostApplyFunction(method: Method) {
            method.trySetAccessible()
            if (method.parameterCount > 0)
                throw IllegalArgumentException("PostApply method '$method' should not take in any arguments!")
            applyFunctions += method
        }

        private fun checkPostRemoveFunction(method: Method) {
            method.trySetAccessible()
            if (method.parameterCount > 0)
                throw IllegalArgumentException("PostRemove method '$method' should not take in any arguments!")
            removeFunctions += method
        }
    }

}