package dev.asodesu.origami.utilities

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun <T> getCatching(func: () -> T): T? {
    return try { func() }
    catch (_: Exception) { null }
}

private const val MSPT = 50

val Int.ticks: Duration get() = (this * MSPT).milliseconds
val Long.ticks: Duration get() = (this * MSPT).milliseconds

val Duration.ticksLong: Long get() = this.inWholeMilliseconds / MSPT
val Duration.ticks: Int get() = ticksLong.toInt()