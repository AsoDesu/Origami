package dev.asodesu.origami.utilities

import kotlin.time.Duration

class Interpolation<T>(val start: T, val end: T, val duration: Duration, private val lerp: (Float) -> T) {
    var progress = 0
        private set
    var isFinished = false
        private set

    fun tick(): T {
        val i = progress / duration.ticks.toFloat()
        progress++
        if (progress >= duration.ticks) isFinished = true
        return lerp(i)
    }

    companion object {
        fun float(start: Float, end: Float, duration: Duration) = Interpolation(start, end, duration) {
            lerp(start, end, it)
        }

        fun double(start: Double, end: Double, duration: Duration) = Interpolation(start, end, duration) {
            lerp(start, end, it.toDouble())
        }
    }

}