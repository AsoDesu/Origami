package dev.asodesu.origami.engine.scene

import dev.asodesu.origami.engine.player.container
import dev.asodesu.origami.engine.scopes.scope
import dev.asodesu.origami.engine.wiring.annotations.Subscribe
import dev.asodesu.origami.utilities.bukkit.allPlayers
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

abstract class OnlineGlobalScene : Scene(), PlayerScene<Player>, Listener {
    override val players = allPlayers

    override fun init() {
        super.init()
        allPlayers.forEach { addPlayer(it) }
    }

    override fun addPlayer(player: Player) {
        scope(this) { setupComponents(player) }
    }

    override fun removePlayer(player: Player) {
        val behaviourContainer = player.container
        behaviourContainer.behaviours.removeIf {
            if (it.internalScope == this) {
                behaviourContainer.destroyBehaviour(it)
                true
            } else false
        }
    }

    @Subscribe
    private fun join(evt: PlayerJoinEvent) = addPlayer(evt.player)

    @Subscribe
    private fun quit(evt: PlayerQuitEvent) = removePlayer(evt.player)

    override fun filter(event: Event) = true
}