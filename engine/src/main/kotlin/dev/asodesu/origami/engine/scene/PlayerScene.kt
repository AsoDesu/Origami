package dev.asodesu.origami.engine.scene

import org.bukkit.OfflinePlayer

interface PlayerScene<P : OfflinePlayer> {
    val players: Collection<P>
    fun addPlayer(player: P)
    fun removePlayer(player: P)
    fun setupComponents(player: P)
}