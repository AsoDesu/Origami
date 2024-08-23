package dev.asodesu.origami.engine

import dev.asodesu.origami.engine.player.container
import dev.asodesu.origami.engine.scopes.SceneScope
import dev.asodesu.origami.utilities.bukkit.cooldown.CooldownContainer
import dev.asodesu.origami.utilities.bukkit.cooldown.CooldownHandler
import org.bukkit.OfflinePlayer
import kotlin.time.Duration

class CooldownBehaviour : Behaviour(), CooldownHandler by CooldownContainer()

fun BehaviourApplicable.checkCooldown(key: String, time: Duration) =
    this.getOrAdd<CooldownBehaviour>(SceneScope.global).checkCooldown(key, time)
fun BehaviourApplicable.consumeCooldown(key: String, time: Duration) =
    this.getOrAdd<CooldownBehaviour>(SceneScope.global).consumeCooldown(key, time)

fun OfflinePlayer.checkCooldown(key: String, time: Duration) = this.container.checkCooldown(key, time)
fun OfflinePlayer.consumeCooldown(key: String, time: Duration) = this.container.consumeCooldown(key, time)