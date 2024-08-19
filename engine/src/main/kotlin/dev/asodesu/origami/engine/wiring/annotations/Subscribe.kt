package dev.asodesu.origami.engine.wiring.annotations

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

annotation class Subscribe(
    val priority: EventPriority = EventPriority.NORMAL,
    val ignoreCanceled: Boolean = false,
    val ignoreFilter: Boolean = false
)
