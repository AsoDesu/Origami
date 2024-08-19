package dev.asodesu.origami.utilities

fun lerp(start: Double, end: Double, t: Double) = start * (1-t) + end * t
fun lerp(start: Float, end: Float, t: Float) = start * (1-t) + end * t