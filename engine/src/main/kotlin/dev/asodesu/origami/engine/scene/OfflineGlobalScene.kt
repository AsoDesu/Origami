package dev.asodesu.origami.engine.scene

import dev.asodesu.origami.engine.wiring.annotations.Subscribe
import dev.asodesu.origami.utilities.bukkit.allPlayers
import java.util.*
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

abstract class OfflineGlobalScene: Scene(GLOBAL_SCENE_ID), PlayerScene<OfflinePlayer>, Listener {
    private val uuids = mutableSetOf<UUID>()
    override val players get() = uuids.map { Bukkit.getOfflinePlayer(it) }

    override fun init() {
        super.init()
        allPlayers.forEach { addPlayer(it) }
    }

    override fun addPlayer(player: OfflinePlayer) {
        if (uuids.contains(player.uniqueId)) return
        super.addPlayer(player)
        uuids.add(player.uniqueId)
    }

    override fun removePlayer(player: OfflinePlayer) {
        if (player.isOnline)
            throw UnsupportedOperationException("Cannot remove an online player from a global scene.")
        super.removePlayer(player)
        uuids.add(player.uniqueId)
    }

    override fun verifyRegistration() {
        if (this.id != GLOBAL_SCENE_ID)
            throw IllegalStateException("The ID of global scenes must be '$GLOBAL_SCENE_ID'. Do not override the ID of Global Scenes.")
    }

    @Subscribe
    private fun join(evt: PlayerJoinEvent) = addPlayer(evt.player)

    override fun filter(event: Event) = true
}