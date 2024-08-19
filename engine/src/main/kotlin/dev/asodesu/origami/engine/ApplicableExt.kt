package dev.asodesu.origami.engine

inline fun <reified T : Behaviour> BehaviourApplicable.getOrNull() = this.getOrNull(T::class)
inline fun <reified T : Behaviour> BehaviourApplicable.get() = this.get(T::class)
inline fun <reified T : Behaviour> BehaviourApplicable.getOrAdd(noinline creator: BehaviourCreator<T>? = null) = this.getOrAdd(T::class, creator)
inline fun <reified T : Behaviour> BehaviourApplicable.has() = this.has(T::class)
inline fun <reified T : Behaviour> BehaviourApplicable.replace(instance: T? = null) = this.replace(T::class, instance)
inline fun <reified T : Behaviour> BehaviourApplicable.add(instance: T? = null) = this.add(T::class, instance)
inline fun <reified T : Behaviour> BehaviourApplicable.remove() = this.remove(T::class)

inline fun <A : BehaviourApplicable, reified T : Behaviour> A.replaceBy(func: (A) -> T) = this.replace(T::class, func(this))
inline fun <A : BehaviourApplicable, reified T : Behaviour> A.addBy(func: (A) -> T) = this.add(T::class, func(this))