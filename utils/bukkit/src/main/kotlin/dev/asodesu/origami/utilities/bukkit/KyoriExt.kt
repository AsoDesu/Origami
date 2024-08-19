package dev.asodesu.origami.utilities.bukkit

import io.papermc.paper.math.Position
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.sound.Sound
import org.bukkit.Location
import org.bukkit.entity.Entity

fun Audience.play(sound: Sound, location: Position) = this.playSound(sound, location.x(), location.y(), location.z())
fun Audience.play(sound: Sound, from: Entity) = this.play(sound, from.location)