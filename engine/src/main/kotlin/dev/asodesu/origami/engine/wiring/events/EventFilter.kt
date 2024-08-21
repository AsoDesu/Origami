package dev.asodesu.origami.engine.wiring.events

import org.bukkit.event.Event

interface EventFilter {
    fun filter(event: Event): Boolean

    companion object {
        fun all(vararg filters: EventFilter) = object : EventFilter {
            override fun filter(event: Event): Boolean {
                return filters.all { it.filter(event) }
            }
        }
    }
}