package dev.asodesu.origami.engine.player

import org.bukkit.OfflinePlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

object PlayerBehaviourRegistry : Listener {
    private val players = mutableMapOf<UUID, PlayerBehaviourContainer>()

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