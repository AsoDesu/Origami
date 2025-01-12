package dev.asodesu.origami.scene

import dev.asodesu.origami.behaviours.Behaviour
import dev.asodesu.origami.objects.ScopedGameObject
import java.lang.UnsupportedOperationException
import kotlin.reflect.KClass

object GlobalScene : Scene() {
    override val name = "_global"

    override fun dispose() {
        throw UnsupportedOperationException("Cannot dispose GlobalScene")
    }

    override fun isChildOf(parentScene: Scene) = false // global scene does not have a parent
}