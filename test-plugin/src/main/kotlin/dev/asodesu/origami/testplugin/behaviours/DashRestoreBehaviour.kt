package dev.asodesu.origami.testplugin.behaviours

import dev.asodesu.origami.engine.consumeCooldown
import dev.asodesu.origami.engine.player.PlayerBehaviour
import dev.asodesu.origami.engine.player.PlayerBehaviourContainer
import dev.asodesu.origami.engine.wiring.annotations.Subscribe
import dev.asodesu.origami.utilities.play
import dev.asodesu.origami.utilities.sendTitle
import dev.asodesu.origami.utilities.ticks
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.math.min

class DashRestoreBehaviour(c: PlayerBehaviourContainer, var restoreAmount: Int = 2) : PlayerBehaviour(c) {
    private val dashBehaviour by lazy { get<DashBehaviour>() }

    @Subscribe
    fun interact(evt: PlayerInteractEvent) {
        val item = evt.item ?: return
        if (item.type != Material.PHANTOM_MEMBRANE) return
        if (player.consumeCooldown("interact", 1.ticks)) return
        item.subtract(1)
        evt.isCancelled = true

        dashBehaviour.dashInventory = min(restoreAmount, dashBehaviour.maxDashInventory)
        player.sendTitle(subtitle = "<gray>Restored <red>$restoreAmount</red> dashes.")
        player.play("block.respawn_anchor.charge", pitch = 1.3f)
        player.swingMainHand()
    }

}