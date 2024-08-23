package dev.asodesu.origami.utilities.bukkit

import java.util.*
import org.bukkit.OfflinePlayer
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityEvent
import org.bukkit.event.entity.PlayerLeashEntityEvent
import org.bukkit.event.hanging.HangingEvent
import org.bukkit.event.inventory.InventoryEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryPickupItemEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.server.ServerEvent
import org.bukkit.event.vehicle.VehicleEvent
import org.bukkit.event.weather.WeatherEvent
import org.bukkit.event.world.WorldEvent
import org.bukkit.inventory.BlockInventoryHolder
import org.bukkit.inventory.Inventory

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

fun Event.filterUUIDs(uuids: Set<UUID>): Boolean {
    return when (this) {
        is EntityDamageByEntityEvent -> uuids.contains(this.damager.uniqueId)
        is PlayerEvent -> uuids.contains(this.player.uniqueId)
        is EntityEvent -> uuids.contains(this.entity.uniqueId)
        is InventoryInteractEvent -> uuids.contains(this.whoClicked.uniqueId)
        is BlockPlaceEvent -> uuids.contains(this.player.uniqueId)
        is BlockBreakEvent -> uuids.contains(this.player.uniqueId)
        else -> true
    }
}

fun Event.filterWorld(world: World): Boolean {
    if (this is InventoryEvent && filterInventory(this.inventory, world)) return true
    return when(this) {
        // general events
        is BlockEvent -> block.world == world
        is HangingEvent -> entity.world == world
        is EntityEvent -> entity.world == world
        is ServerEvent -> true
        is VehicleEvent -> vehicle.world == world
        is WeatherEvent -> this.world == world
        is WorldEvent -> this.world == world
        is InventoryInteractEvent -> whoClicked.world == world

        // this one stupid event
        is PlayerChangedWorldEvent -> from == world || player.world == world
        is PlayerEvent -> player.world == world

        // specific events cuz FUCK YOU PAPER
        is PlayerLeashEntityEvent -> player.world == world

        is InventoryMoveItemEvent -> filterInventory(this.initiator, world)
        is InventoryPickupItemEvent -> filterInventory(this.inventory, world)
        else -> true
    }
}

private fun filterInventory(inventory: Inventory, world: World): Boolean {
    val holder = inventory.holder
    if ((holder as? Entity)?.world == world) return true
    if ((holder as? BlockInventoryHolder)?.block?.world == world) return true
    return false
}