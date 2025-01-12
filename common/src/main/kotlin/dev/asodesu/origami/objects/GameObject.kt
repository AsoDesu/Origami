package dev.asodesu.origami.objects

import dev.asodesu.origami.objects.applicable.BehaviourApplicable
import dev.asodesu.origami.objects.repository.GameObjectNode
import dev.asodesu.origami.scene.GlobalScene
import dev.asodesu.origami.scene.Scene
import dev.asodesu.origami.util.ANSI_BLACK
import dev.asodesu.origami.util.ANSI_BLACK_BRIGHT
import dev.asodesu.origami.util.ANSI_BLUE
import dev.asodesu.origami.util.ANSI_CYAN
import dev.asodesu.origami.util.ANSI_CYAN_BRIGHT
import dev.asodesu.origami.util.ANSI_PURPLE
import dev.asodesu.origami.util.ANSI_PURPLE_BRIGHT
import dev.asodesu.origami.util.ANSI_RED
import dev.asodesu.origami.util.ANSI_RESET
import dev.asodesu.origami.util.ANSI_YELLOW
import dev.asodesu.origami.util.Disposable

interface GameObject : BehaviourApplicable, GameObjectNode, Disposable {
    /**
     * A name to reference this game object
     */
    override var name: String

    var scene: Scene
    var parent: GameObjectNode?
    val children: List<GameObject>

    fun global() = scene(GlobalScene)
    fun global(func: ScopedGameObject.() -> Unit) = scene(GlobalScene).also(func)
    fun scene(value: Scene, func: ScopedGameObject.() -> Unit) = scene(value).also(func)
    override fun scene(value: Scene): ScopedGameObject

    // debug
    fun debugInfo(): String {
        return buildString {
            appendLine("${ANSI_CYAN_BRIGHT}✂ $name ${ANSI_BLUE}<${scene.name}>${ANSI_RESET}")
            append(appliedBehavioursDebug())
            if (children.isNotEmpty()) {
                appendLine("${ANSI_BLACK_BRIGHT}|${ANSI_RESET}")
                children.forEach {
                    appendLine(it.debugInfo().prependIndent("${ANSI_BLACK_BRIGHT}|${ANSI_RESET}  "))
                }
            }
        }.trimEnd()
    }
}