package dev.asodesu.origami.utilities.bukkit

import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

fun Listener.register(againstPlugin: Plugin = plugin) {
    pluginManager.registerEvents(this, againstPlugin)
}

fun Listener.unregister() {
    HandlerList.unregisterAll(this)
}