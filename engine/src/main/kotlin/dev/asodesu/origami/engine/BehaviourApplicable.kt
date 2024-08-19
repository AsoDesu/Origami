package dev.asodesu.origami.engine

import dev.asodesu.origami.utilities.bukkit.cooldown.CooldownHandler
import kotlin.reflect.KClass

typealias BehaviourCreator<T> = (BehaviourApplicable) -> T

interface BehaviourApplicable {
    val behaviours: Collection<Behaviour>
    val cooldowns: CooldownHandler

    fun <T : Behaviour> getOrNull(clazz: KClass<T>): T?
    fun <T : Behaviour> get(clazz: KClass<T>): T
    fun <T : Behaviour> getOrAdd(clazz: KClass<T>, creator: BehaviourCreator<T>? = null): T
    fun <T : Behaviour> has(clazz: KClass<T>): Boolean
    fun <T : Behaviour> replace(clazz: KClass<T>, instance: T? = null): T
    fun <T : Behaviour> add(clazz: KClass<T>, instance: T? = null): T
    fun <T : Behaviour> remove(clazz: KClass<T>): T?
}