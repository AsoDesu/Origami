package dev.asodesu.origami.utilities.bukkit

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.util.Vector

fun Player.particle(
    type: Particle,
    location: Location,
    count: Int = 1,
    offset: Vector = Vector(0.0, 0.0, 0.0),
    extra: Double = 1.0,
    data: Any? = null
) = this.spawnParticle(
    type,
    location,
    count,
    offset.x, offset.y, offset.z,
    extra,
    data
)

fun World.particle(
    type: Particle,
    location: Location,
    count: Int = 1,
    offset: Vector = Vector(0.0, 0.0, 0.0),
    extra: Double = 1.0,
    data: Any? = null
) = this.spawnParticle(
    type,
    location,
    count,
    offset.x, offset.y, offset.z,
    extra,
    data
)