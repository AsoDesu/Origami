package dev.asodesu.origami.utilities

val Class<*>.allMethods get() = declaredMethods.toSet() union methods.toSet()