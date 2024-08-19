package dev.asodesu.origami.testplugin.behaviours

import dev.asodesu.origami.engine.consumeCooldown
import dev.asodesu.origami.engine.player.PlayerBehaviour
import dev.asodesu.origami.engine.player.PlayerBehaviourContainer
import dev.asodesu.origami.engine.wiring.annotations.Subscribe
import dev.asodesu.origami.utilities.bukkit.addPotionEffect
import dev.asodesu.origami.utilities.miniMessage
import dev.asodesu.origami.utilities.play
import dev.asodesu.origami.utilities.ticks
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random

class InstantHealthBehaviour(c: PlayerBehaviourContainer) : PlayerBehaviour(c) {
    companion object {
        val KEY_INSTANT_POTION = NamespacedKey("asodesu", "is_instant_potion")
        fun getInstantHealth(amplifier: Int = 2): ItemStack {
            val item = ItemStack(Material.POTION, 64)
            item.editMeta(PotionMeta::class.java) {
                it.setEnchantmentGlintOverride(true)
                it.color = Color.RED
                it.setMaxStackSize(64)
                it.itemName(miniMessage("<red><u>Instant Health</u></red> <gray>(Right Click)"))
                it.addPotionEffect(
                    PotionEffectType.INSTANT_HEALTH,
                    duration = 1.ticks,
                    amplifier = amplifier,
                )
                it.persistentDataContainer.set(KEY_INSTANT_POTION, PersistentDataType.BOOLEAN, true)
            }
            return item
        }
    }

    @Subscribe
    private fun interact(evt: PlayerInteractEvent) {
        val item = evt.item ?: return
        if (item.type != Material.POTION) return
        if (!evt.action.isRightClick) return
        if (player.consumeCooldown("interact", 1.ticks)) return

        val meta = item.itemMeta as? PotionMeta ?: return
        if (!meta.persistentDataContainer.has(KEY_INSTANT_POTION)) return

        evt.isCancelled = true
        item.subtract(1)

        meta.basePotionType?.potionEffects?.let { player.addPotionEffects(it) } // add main effects
        player.addPotionEffects(meta.customEffects) // add custom effects

        val drink = Random.nextDouble(0.9, 1.1).toFloat()
        player.play("entity.generic.drink", pitch = drink)
    }

}