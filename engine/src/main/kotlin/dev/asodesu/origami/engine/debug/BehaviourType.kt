package dev.asodesu.origami.engine.debug

import dev.asodesu.origami.engine.impl.BehaviourContainer
import dev.asodesu.origami.engine.player.PlayerBehaviourRegistry
import java.util.*
import org.bukkit.Bukkit

enum class BehaviourType(val values: () -> List<String>, val getter: (String) -> BehaviourContainer?) {
    PLAYER(Values::getPlayers, Values::getPlayer);

    object Values {
        fun getPlayers(): List<String> {
            return PlayerBehaviourRegistry.players.map { it.value.offlinePlayer.name ?: it.key.toString() }
        }
        fun getPlayer(input: String): BehaviourContainer? {
            return if (input.length == 36) {
                PlayerBehaviourRegistry.players[UUID.fromString(input)]
            } else {
                Bukkit.getOfflinePlayerIfCached(input)
                    ?.let { PlayerBehaviourRegistry.players[it.uniqueId] }
            }
        }
    }
}