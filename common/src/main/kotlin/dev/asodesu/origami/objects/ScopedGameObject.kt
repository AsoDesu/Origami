package dev.asodesu.origami.objects

import dev.asodesu.origami.behaviours.Behaviour
import dev.asodesu.origami.objects.applicable.BehaviourWritable
import kotlin.reflect.KClass

interface ScopedGameObject : GameObject, BehaviourWritable