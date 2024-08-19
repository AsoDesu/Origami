package dev.asodesu.origami.engine

import dev.asodesu.origami.engine.scopes.Scope

inline fun <reified T : Behaviour> BehaviourApplicable.getOrNull() = this.getOrNull(T::class)
inline fun <reified T : Behaviour> BehaviourApplicable.get() = this.get(T::class)
inline fun <reified T : Behaviour> BehaviourApplicable.getOrAdd(scope: Scope? = null, noinline creator: BehaviourCreator<T>? = null) = this.getOrAdd(T::class, scope, creator)
inline fun <reified T : Behaviour> BehaviourApplicable.has() = this.has(T::class)
inline fun <reified T : Behaviour> BehaviourApplicable.replace(instance: T? = null, scope: Scope? = null) = this.replace(T::class, instance, scope)
inline fun <reified T : Behaviour> BehaviourApplicable.add(instance: T? = null, scope: Scope? = null) = this.add(T::class, instance, scope)
inline fun <reified T : Behaviour> BehaviourApplicable.remove() = this.remove(T::class)

inline fun <A : BehaviourApplicable, reified T : Behaviour> A.replaceBy(scope: Scope? = null, func: (A) -> T) = this.replace(T::class, func(this), scope)
inline fun <A : BehaviourApplicable, reified T : Behaviour> A.addBy(scope: Scope? = null, func: (A) -> T) = this.add(T::class, func(this), scope)