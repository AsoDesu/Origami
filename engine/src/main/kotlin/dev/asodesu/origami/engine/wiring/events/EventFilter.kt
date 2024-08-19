package dev.asodesu.origami.engine.wiring.events

import org.bukkit.event.Event

interface EventFilter {
    fun filter(event: Event): Boolean
}