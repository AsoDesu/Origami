package dev.asodesu.origami.engine.scene

import dev.asodesu.origami.engine.player.container
import dev.asodesu.origami.engine.scopes.SceneScope
import dev.asodesu.origami.engine.scopes.scope
import org.bukkit.OfflinePlayer

interface PlayerScene<P : OfflinePlayer> : SceneScope {
    val players: Collection<P>
    fun addPlayer(player: P) {
        scope(this) { setupComponents(player) }
    }

    fun removePlayer(player: P) {
        val behaviourContainer = player.container
        behaviourContainer.behaviours.toList().forEach {
            if (it.internalScope == this) {
                behaviourContainer.destroyBehaviour(it)
            }
        }
    }
    fun setupComponents(player: P)

    fun forEachPlayer(func: (P) -> Unit) {
        players.forEach(func)
    }
}