package dev.asodesu.origami.engine.player

import java.util.*
import org.bukkit.OfflinePlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object PlayerBehaviourRegistry : Listener {
    val players = mutableMapOf<UUID, PlayerBehaviourContainer>()

    fun getPlayerContainer(player: OfflinePlayer): PlayerBehaviourContainer {
        return players.getOrPut(player.uniqueId) { PlayerBehaviourContainer(player.uniqueId) }
    }

    @EventHandler
    fun join(evt: PlayerJoinEvent) {
        getPlayerContainer(evt.player).join()
    }

    @EventHandler
    fun quit(evt: PlayerQuitEvent) {
        getPlayerContainer(evt.player).quit()
    }
}