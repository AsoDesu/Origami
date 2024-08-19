package dev.asodesu.origami.utilities.bukkit.cooldown

import kotlin.time.Duration

interface CooldownHandler {
    fun checkCooldown(key: String, time: Duration): Boolean
    fun consumeCooldown(key: String, time: Duration): Boolean
}