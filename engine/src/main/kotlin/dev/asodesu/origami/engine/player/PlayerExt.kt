package dev.asodesu.origami.engine.player

import org.bukkit.OfflinePlayer

val OfflinePlayer.behaviourContainer get() = PlayerBehaviourRegistry.getPlayerContainer(this)