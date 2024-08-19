package dev.asodesu.origami.utilities

import net.kyori.adventure.key.Key

fun key(namespace: String, string: String) = Key.key(namespace, string)
fun key(string: String) = Key.key(string)