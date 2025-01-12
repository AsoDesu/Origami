package dev.asodesu.origami.objects.entity

import dev.asodesu.origami.behaviours.Behaviour
import org.bukkit.entity.Entity

open class EntityHolder<T : Entity>(val entity: T) : Behaviour()