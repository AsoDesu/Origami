package dev.asodesu.origami.utilities

import java.lang.reflect.Method

val Class<*>.allMethods get() = resolveAllMethods()

private fun Class<*>.resolveAllMethods(): Set<Method> {
    val allMethods = this.declaredMethods.toMutableSet()
    val superClass = this.superclass
    if (superClass != null) allMethods += superClass.resolveAllMethods()
    return allMethods
}