package dev.asodesu.origami.utilities.bukkit.cooldown

import dev.asodesu.origami.utilities.bukkit.tickNumber
import dev.asodesu.origami.utilities.ticks
import kotlin.time.Duration

class SimpleCooldowns : CooldownHandler {
    private val cooldownMap = mutableMapOf<String, Int>()

    override fun checkCooldown(key: String, time: Duration): Boolean {
        val i = cooldownMap[key] ?: return false
        return (tickNumber - i) <= time.ticks
    }

    override fun consumeCooldown(key: String, time: Duration): Boolean {
        if (checkCooldown(key, time)) return true
        cooldownMap[key] = tickNumber
        return false
    }
}