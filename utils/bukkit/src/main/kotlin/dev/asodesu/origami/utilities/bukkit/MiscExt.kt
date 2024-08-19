package dev.asodesu.origami.utilities.bukkit

import org.bukkit.Location
import dev.asodesu.origami.utilities.lerp
import dev.asodesu.origami.utilities.miniMessage

fun Location.off(x: Int = 0, y: Int = 0, z: Int = 0, yaw: Float = getYaw(), pitch: Float = getPitch())
        = this.off(x.toDouble(), y.toDouble(), z.toDouble(), yaw, pitch)

fun Location.off(x: Long = 0, y: Long = 0, z: Long = 0, yaw: Float = getYaw(), pitch: Float = getPitch())
        = this.off(x.toDouble(), y.toDouble(), z.toDouble(), yaw, pitch)

fun Location.off(x: Float = 0f, y: Float = 0f, z: Float = 0f, yaw: Float = getYaw(), pitch: Float = getPitch())
        = this.off(x.toDouble(), y.toDouble(), z.toDouble(), yaw, pitch)

fun Location.off(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0, yaw: Float = getYaw(), pitch: Float = getPitch())
    = Location(world, x() + x, y() + y, z() + z, yaw, pitch)

fun locationLerp(start: Location, end: Location, t: Double): Location {
    return Location(
        start.world,
        lerp(start.x, end.x, t),
        lerp(start.y, end.y, t),
        lerp(start.z, end.z, t),
        lerp(start.yaw, end.yaw, t.toFloat()),
        lerp(start.pitch, end.pitch, t.toFloat()),
    )
}