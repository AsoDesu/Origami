package dev.asodesu.origami.engine.player

import org.bukkit.OfflinePlayer

val OfflinePlayer.container get() = PlayerBehaviourRegistry.getPlayerContainer(this)