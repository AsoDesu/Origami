package dev.asodesu.origami.objects.player

import dev.asodesu.origami.objects.GameObject
import dev.asodesu.origami.objects.builder.GameObject
import dev.asodesu.origami.objects.repository.GameObjectNode
import dev.asodesu.origami.scene.Scene
import java.util.UUID
import org.bukkit.entity.Player

object PlayerObjectRepository : GameObjectNode {
    private val objects = mutableMapOf<UUID, GameObject>()

    fun getPlayer(player: Player): GameObject {
        return objects.getOrPut(player.uniqueId) { createPlayer(player) }
    }

    private fun createPlayer(player: Player) = GameObject {
        name("player_${player.name}")
        behaviours(Scene.GLOBAL) {
            add(PlayerHolder(player))
        }
    }

    override fun remove(gameObject: GameObject) {}
    override fun dispose() {}
}