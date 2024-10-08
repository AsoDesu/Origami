package dev.asodesu.origami.utilities.bukkit

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

lateinit var plugin: JavaPlugin
val pluginLogger get() = plugin.slF4JLogger
val dataFolder get() = plugin.dataFolder

val server get() = Bukkit.getServer()
val pluginManager get() = server.pluginManager
val scheduler get() = server.scheduler
val consoleSender get() = server.consoleSender
val allPlayers: MutableCollection<out Player> get() = server.onlinePlayers
val tickNumber get() = server.currentTick

val Player.offlinePlayerReference get() = Bukkit.getOfflinePlayer(this.uniqueId)