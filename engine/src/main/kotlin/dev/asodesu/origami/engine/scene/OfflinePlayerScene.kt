package dev.asodesu.origami.engine.scene

import dev.asodesu.origami.utilities.bukkit.filterUUIDs
import java.util.*
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.event.Event
import org.bukkit.event.Listener

abstract class OfflinePlayerScene(id: String) : Scene(id), PlayerScene<OfflinePlayer>, Listener {
    protected val uuids = mutableSetOf<UUID>()
    override val players get() = uuids.map { Bukkit.getOfflinePlayer(it) }

    override fun addPlayer(player: OfflinePlayer) {
        if (uuids.contains(player.uniqueId)) return
        uuids.add(player.uniqueId)
        super.addPlayer(player)
    }

    override fun removePlayer(player: OfflinePlayer) {
        if (!uuids.contains(player.uniqueId)) return
        super.removePlayer(player)
        uuids.remove(player.uniqueId)
    }

    override fun getDebugInfo() = buildString {
        appendLine(super.getDebugInfo())
        appendLine("Players: <origami>[${uuids.size} added]</origami>")
        uuids.forEach {
            appendLine(" - <origami>${it}</origami>")
        }
    }.trim()

    override fun filter(event: Event) = event.filterUUIDs(uuids)
}