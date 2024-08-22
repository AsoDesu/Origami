package dev.asodesu.origami.engine.player

import dev.asodesu.origami.engine.debug.Debuggable
import dev.asodesu.origami.engine.impl.BehaviourContainer
import java.util.*
import org.bukkit.Bukkit

class PlayerBehaviourContainer(private val uuid: UUID) : BehaviourContainer(), Debuggable {
    internal val offlinePlayer get() = Bukkit.getOfflinePlayer(uuid)
    internal val playerOrNull get() = Bukkit.getPlayer(uuid)
    internal val player get() = playerOrNull ?: throw IllegalAccessException("Attempted to access player object while player was offline.")
    internal val isOnline: Boolean get() = offlinePlayer.isOnline

    fun join() {
    }

    fun quit() {
    }

    override fun getDebugInfo() = buildString {
        appendLine("UUID: <red>$uuid</red>")
        appendLine("Username: <gold>${offlinePlayer.name ?: "unknown"}</gold>")
        appendLine("IsOnline: <yellow>$isOnline</yellow>")
        appendLine(super.getDebugInfo())
    }.trim()
}