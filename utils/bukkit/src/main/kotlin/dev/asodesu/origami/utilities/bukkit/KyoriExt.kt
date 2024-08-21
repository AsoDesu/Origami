package dev.asodesu.origami.utilities.bukkit

import dev.asodesu.origami.utilities.debug
import io.papermc.paper.math.Position
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.sound.Sound
import org.bukkit.entity.Entity

fun Audience.play(sound: Sound, location: Position) = this.playSound(sound, location.x(), location.y(), location.z())
fun Audience.play(sound: Sound, from: Entity) = this.play(sound, from.location)

val debugAudience: Audience
    get() = Audience.audience(allPlayers.filter { it.hasPermission("origami.debug") } + consoleSender)

fun debug(str: String) = debugAudience.debug(str)