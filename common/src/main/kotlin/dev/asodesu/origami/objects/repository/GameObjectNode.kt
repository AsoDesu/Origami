package dev.asodesu.origami.objects.repository

import dev.asodesu.origami.objects.GameObject

/**
 * An interface representing a repository of GameObjects
 */
interface GameObjectNode {
    fun addChild(childObject: GameObject)
    fun removeChild(childObject: GameObject)
}