package dev.asodesu.origami.behaviours.factory

import dev.asodesu.origami.behaviours.Behaviour
import kotlin.reflect.KClass

object BehaviourFactory {

    fun <T : Behaviour> construct(clazz: KClass<T>): T {
        TODO("Construct behaviours")
    }

}