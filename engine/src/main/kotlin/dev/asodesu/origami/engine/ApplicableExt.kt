package dev.asodesu.origami.engine

import dev.asodesu.origami.engine.scopes.SceneScope

inline fun <reified T : Behaviour> BehaviourApplicable.getOrNull() = this.getOrNull(T::class)
inline fun <reified T : Behaviour> BehaviourApplicable.get() = this.get(T::class)
inline fun <reified T : Behaviour> BehaviourApplicable.getOrAdd(scope: SceneScope? = null, noinline creator: BehaviourCreator<T>? = null) = this.getOrAdd(T::class, scope, creator)
inline fun <reified T : Behaviour> BehaviourApplicable.has() = this.has(T::class)
inline fun <reified T : Behaviour> BehaviourApplicable.replace(instance: T? = null, scope: SceneScope? = null) = this.replace(T::class, instance, scope)
inline fun <reified T : Behaviour> BehaviourApplicable.add(instance: T? = null, scope: SceneScope? = null) = this.add(T::class, instance, scope)
inline fun <reified T : Behaviour> BehaviourApplicable.removeFirst() = this.removeFirst(T::class)
inline fun <reified T : Behaviour> BehaviourApplicable.removeAll() = this.removeAll(T::class)

inline fun <A : BehaviourApplicable, reified T : Behaviour> A.replaceBy(scope: SceneScope? = null, func: (A) -> T) = this.replace(T::class, func(this), scope)
inline fun <A : BehaviourApplicable, reified T : Behaviour> A.addBy(scope: SceneScope? = null, func: (A) -> T) = this.add(T::class, func(this), scope)