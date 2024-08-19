package dev.asodesu.origami.utilities.bukkit

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityEvent
import org.bukkit.event.inventory.InventoryEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.player.PlayerEvent

fun Event.filterPlayer(player: OfflinePlayer): Boolean {
    return when (this) {
        is EntityDamageByEntityEvent -> this.damager == player
        is PlayerEvent -> this.player == player
        is EntityEvent -> this.entity == player
        is InventoryInteractEvent -> this.whoClicked == player
        is BlockPlaceEvent -> this.player == player
        is BlockBreakEvent -> this.player == player
        else -> true
    }
}
