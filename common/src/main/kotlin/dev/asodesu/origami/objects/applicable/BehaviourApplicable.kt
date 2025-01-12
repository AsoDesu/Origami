package dev.asodesu.origami.objects.applicable

import dev.asodesu.origami.behaviours.Behaviour
import dev.asodesu.origami.scene.Scene
import dev.asodesu.origami.util.ANSI_BLACK_BRIGHT
import dev.asodesu.origami.util.ANSI_BLUE
import dev.asodesu.origami.util.ANSI_PURPLE_BRIGHT
import dev.asodesu.origami.util.ANSI_RESET
import kotlin.reflect.KClass

interface BehaviourApplicable {
    val name: String

    fun scene(value: Scene): BehaviourWritable

    fun <T : Behaviour> get(clazz: KClass<T>): T {
        return getOrNull(clazz)
            ?: throw NoSuchElementException("Behaviour ${clazz.qualifiedName} is not assigned to this object.")
    }

    fun remove(behaviour: Behaviour)
    fun removeIf(predicate: (Behaviour) -> Boolean)
    fun remove(clazz: KClass<out Behaviour>) = removeIf { clazz.isInstance(it) }
    fun remove(scene: Scene) = removeIf { it.internalScene == scene }
    operator fun minusAssign(behaviour: Behaviour) = remove(behaviour)

    fun contains(clazz: KClass<out Behaviour>): Boolean
    fun <T : Behaviour> getOrNull(clazz: KClass<T>): T?
    fun <T : Behaviour> getAll(clazz: KClass<T>): List<T>
    fun getAll(): List<Behaviour>

    fun appliedBehavioursDebug() = buildString {
        val behaviours = getAll()
        if (behaviours.isNotEmpty()) {
            behaviours.forEach {
                appendLine("$ANSI_BLACK_BRIGHT|$ANSI_RESET  ${ANSI_PURPLE_BRIGHT}☄ $it $ANSI_BLUE<${it.internalScene?.name}>$ANSI_RESET")
            }
        }
    }
}