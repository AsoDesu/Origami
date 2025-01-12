package dev.asodesu.origami.behaviours

import dev.asodesu.origami.behaviours.delegates.BehaviourAppliedObjectRef
import dev.asodesu.origami.behaviours.delegates.BehaviourSceneRef
import dev.asodesu.origami.behaviours.delegates.ObjectBehaviourRef
import dev.asodesu.origami.objects.GameObject
import dev.asodesu.origami.objects.applicable.BehaviourApplicable
import dev.asodesu.origami.objects.applicable.BehaviourWritable
import dev.asodesu.origami.scene.GlobalScene
import dev.asodesu.origami.scene.Scene
import kotlin.reflect.jvm.jvmName

abstract class Behaviour {
    protected val appliedTo get() = internalAppliedTo
        ?: throw IllegalStateException("This behaviour has not been applied to an object yet.")
    protected val scene get() = internalScene
        ?: throw IllegalStateException("This behaviour has not been applied to an object yet.")

    internal var internalScene: Scene? = null
    internal var internalAppliedTo: BehaviourApplicable? = null
    internal fun apply(applicable: BehaviourApplicable, scene: Scene) {
        this.internalAppliedTo = applicable
        this.internalScene = scene

        postInit()
    }
    internal fun remove() {
        this.internalAppliedTo = null
        this.internalScene = null
        postRemove()
    }

    protected open fun postInit() {}
    protected open fun postRemove() {}

    //<editor-fold desc="Applied To functions">
    protected fun writer(): BehaviourWritable = appliedTo.scene(scene)

    protected inline fun <reified T : Behaviour> add() = writer().add(T::class)
    protected fun add(behaviour: Behaviour) = writer().add(behaviour)

    protected inline fun <reified T : Behaviour> replace() = writer().replace(T::class)
    protected fun replace(behaviour: Behaviour) = writer().replace(behaviour)

    protected inline fun <reified T : Behaviour> getOrAdd() = writer().getOrAdd(T::class)
    //</editor-fold>
    //<editor-fold desc="Game Object - functions">
    protected fun GameObject.writer(): BehaviourWritable = this.scene(scene)

    protected inline fun <reified T : Behaviour> GameObject.add() = writer().add(T::class)
    protected fun GameObject.add(behaviour: Behaviour) = writer().add(behaviour)

    protected inline fun <reified T : Behaviour> GameObject.replace() = writer().replace(T::class)
    protected fun GameObject.replace(behaviour: Behaviour) = writer().replace(behaviour)

    protected inline fun <reified T : Behaviour> GameObject.getOrAdd() = writer().getOrAdd(T::class)
    //</editor-fold>

    //<editor-fold desc="Delegates">
    protected inline fun <reified T : Behaviour> reference() = BehaviourAppliedObjectRef(T::class)
    protected inline fun <reified T : Behaviour> sceneReference() = BehaviourSceneRef(T::class)
    protected inline fun <reified T : Behaviour> BehaviourApplicable.reference() = ObjectBehaviourRef(this, T::class)
    //</editor-fold>

    override fun toString(): String {
        return this::class.simpleName ?: ("jvm/" + this::class.jvmName)
    }
}