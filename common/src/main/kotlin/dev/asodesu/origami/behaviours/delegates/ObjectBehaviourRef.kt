package dev.asodesu.origami.behaviours.delegates

import dev.asodesu.origami.behaviours.Behaviour
import dev.asodesu.origami.objects.applicable.BehaviourApplicable
import java.lang.IllegalStateException
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ObjectBehaviourRef<V : Behaviour>(private val obj: BehaviourApplicable, private val clazz: KClass<V>) : ReadOnlyProperty<Behaviour, V> {
    private var ref: V? = null

    override fun getValue(thisRef: Behaviour, property: KProperty<*>): V {
       if (ref == null || ref?.internalAppliedTo == null)
           return init() ?: throw IllegalStateException("Behaviour '${clazz.qualifiedName}' is not applied to this object.")
        return ref ?: throw IllegalStateException("UnknownError (make an issue): ref is null but we should have just assigned it.")
    }

    private fun init() = obj.getOrNull(clazz).also { ref = it }
}