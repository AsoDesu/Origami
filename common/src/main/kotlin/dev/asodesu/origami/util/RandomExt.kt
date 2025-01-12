package dev.asodesu.origami.util

import java.awt.Color
import kotlin.random.Random

const val alphaNumericCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
fun randomString(len: Int, chars: String = alphaNumericCharacters): String {
    return buildString {
        repeat(len) { append(chars.random()) }
    }
}

fun randomColor(): Color {
    return Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
}