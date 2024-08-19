package dev.asodesu.origami.engine.wiring.events

import dev.asodesu.origami.engine.error.InvalidBehaviourSubscriptionException
import dev.asodesu.origami.engine.wiring.annotations.Subscribe
import dev.asodesu.origami.utilities.allMethods
import dev.asodesu.origami.utilities.bukkit.plugin
import dev.asodesu.origami.utilities.bukkit.pluginManager
import java.lang.reflect.Method
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor

typealias EventMethods = List<FilteredEvents.ListenerMethodData>

object FilteredEvents {

    fun locateAndRegister(listener: Listener, filter: EventFilter) {
        val clazz = listener::class.java
        val methods = locate(clazz, clazz.allMethods)
        return register(listener, filter, methods)
    }

    fun locate(listener: Class<*>, methods: Iterable<Method>): EventMethods {
        return methods.mapNotNull {
            if (!it.isAnnotationPresent(Subscribe::class.java)) return@mapNotNull null
            val subscription = it.getAnnotation(Subscribe::class.java)

            if (it.parameterCount != 1)
                throw InvalidBehaviourSubscriptionException("Error when registering: ${listener.name}, Expected one event parameter but found ${it.parameterCount}")
            val clazz = it.parameterTypes.first()
            if (!Event::class.java.isAssignableFrom(clazz))
                throw InvalidBehaviourSubscriptionException("Error when registering: ${listener.name}, Expected the method parameter to be an event, but it was '${clazz.name}'")

            val eventClass = clazz as Class<out Event>
            ListenerMethodData(it, eventClass, subscription)
        }
    }

    fun register(listener: Listener, filter: EventFilter?, methods: EventMethods) {
        methods.forEach {
            val executorDelegate = EventExecutor.create(it.method, it.eventClass)
            val executor = if (filter == null || it.subscription.ignoreFilter) {
                executorDelegate
            } else {
                FilterExecutor(filter, executorDelegate)
            }
            pluginManager.registerEvent(
                it.eventClass,
                listener,
                it.subscription.priority,
                executor,
                plugin,
                it.subscription.ignoreCanceled
            )
        }
    }

    class FilterExecutor(private val filter: EventFilter, private val delegate: EventExecutor) : EventExecutor {
        override fun execute(listener: Listener, event: Event) {
            if (!filter.filter(event)) return
            delegate.execute(listener, event)
        }
    }

    class ListenerMethodData(val method: Method, val eventClass: Class<out Event>, val subscription: Subscribe)
}