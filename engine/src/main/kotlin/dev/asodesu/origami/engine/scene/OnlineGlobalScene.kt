package dev.asodesu.origami.engine.scene

import dev.asodesu.origami.engine.wiring.annotations.Subscribe
import dev.asodesu.origami.utilities.bukkit.allPlayers
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

abstract class OnlineGlobalScene : Scene(GLOBAL_SCENE_ID), PlayerScene<Player>, Listener {
    override val players get() = allPlayers

    override fun init() {
        super.init()
        allPlayers.forEach { addPlayer(it) }
    }

    override fun verifyRegistration() {
        if (this.id != GLOBAL_SCENE_ID)
            throw IllegalStateException("The ID of global scenes must be '$GLOBAL_SCENE_ID'. Do not override the ID of Global Scenes.")
    }

    @Subscribe
    private fun join(evt: PlayerJoinEvent) = addPlayer(evt.player)

    @Subscribe
    private fun quit(evt: PlayerQuitEvent) = removePlayer(evt.player)

    override fun filter(event: Event) = true
}