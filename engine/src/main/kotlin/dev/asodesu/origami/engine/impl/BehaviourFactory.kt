package dev.asodesu.origami.engine.impl

import dev.asodesu.origami.engine.Behaviour
import dev.asodesu.origami.engine.BehaviourApplicable
import dev.asodesu.origami.engine.BehaviourCreator
import dev.asodesu.origami.engine.error.InvalidBehaviourConstructionException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

object BehaviourFactory {
    private val creators = mutableMapOf<KClass<out Behaviour>, BehaviourCreator<out Behaviour>>()

    fun <T : Behaviour> create(clazz: KClass<T>, applying: BehaviourApplicable): T {
        val instance = when {
            creators.containsKey(clazz) -> {
                val result = creators[clazz]!!.invoke(applying)
                result as? T
                    ?: throw InvalidBehaviourConstructionException("Default behaviour constructor for '${clazz.qualifiedName}' failed because the creator returned '${result::class.java.name}'")
            }
            else -> constructDefault(clazz, applying)
        }
        return instance
    }

    private fun <T : Behaviour> constructDefault(clazz: KClass<T>, applying: BehaviourApplicable): T {
        val applyingClass = applying::class.java
        // get a default construtor for use
        val validConstructors: List<Pair<KFunction<*>, Map<KParameter, Any?>>> = clazz.constructors.mapNotNull map@{
            val params = it.parameters

            // we can always initialise empty constructors
            if (params.isEmpty()) {
                return@map it to mapOf<KParameter, Any?>()
            } else {
                val args = mutableMapOf<KParameter, Any?>()
                params.forEach { param ->
                    // get the type of this parameter
                    val type = param.type.classifier as? KClass<*>
                    // check if this parameter is a BehaviourApplicable
                    //  otherwise we check if this parameter is optional,
                    //  if it is not, then we can't initialise this
                    //  constructor... like at all.
                    if (type != null && type.java.isAssignableFrom(applyingClass)) {
                        args[param] = applying
                    } else if (!param.isOptional) {
                        // uh oh, we have a parameter which is required
                        //  but isn't of a type we can pass in so uhhh
                        //  sorry constructor, but you are *invalid*
                        return@map null
                    }
                }
                return@map it to args
            }
        }
        // if we don't have any, throw exception
        if (validConstructors.isEmpty())
            throw InvalidBehaviourConstructionException("Cannot construct behaviour '${clazz.qualifiedName}' because it does not have a constructor with is compatible with ${applyingClass.simpleName}")

        // the best constructor to use, is generally the one which uses the most arguments
        val (constructor, args) = if (validConstructors.size == 1) validConstructors.first()
        else validConstructors.maxBy { it.second.size }
        return constructor.callBy(args) as T
    }
}