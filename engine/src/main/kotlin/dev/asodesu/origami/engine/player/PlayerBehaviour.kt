package dev.asodesu.origami.engine.player

import dev.asodesu.origami.engine.Behaviour
import dev.asodesu.origami.utilities.bukkit.filterPlayer
import org.bukkit.event.Event

abstract class PlayerBehaviour(private val behaviourContainer: PlayerBehaviourContainer) : Behaviour() {
    protected val offlinePlayer by behaviourContainer::offlinePlayer
    protected val playerOrNull by behaviourContainer::playerOrNull
    protected val player by behaviourContainer::player
    protected val isOnline by behaviourContainer::isOnline

    override fun filter(event: Event) = event.filterPlayer(offlinePlayer)
}