package dev.asodesu.origami.engine

import kotlin.reflect.KClass

typealias BehaviourCreator<T> = (BehaviourApplicable) -> T

interface BehaviourApplicable {
    val behaviours: Collection<Behaviour>

    fun <T : Behaviour> getOrNull(clazz: KClass<T>): T?
    fun <T : Behaviour> get(clazz: KClass<T>): T
    fun <T : Behaviour> getOrAdd(clazz: KClass<T>, creator: BehaviourCreator<T>? = null): T
    fun <T : Behaviour> has(clazz: KClass<T>): Boolean
    fun <T : Behaviour> replace(clazz: KClass<T>, instance: T? = null): T
    fun <T : Behaviour> add(clazz: KClass<T>, instance: T? = null): T
    fun <T : Behaviour> remove(clazz: KClass<T>): T?
}