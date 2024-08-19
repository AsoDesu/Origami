package dev.asodesu.origami.utilities.bukkit

import dev.asodesu.origami.utilities.ticks
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

val infinitePotionDuration = (-1).seconds

fun potionEffect(
    type: PotionEffectType,
    duration: Duration = infinitePotionDuration,
    amplifier: Int = 0,
    ambient: Boolean = false,
    particles: Boolean = false,
    icon: Boolean = false
) = PotionEffect(type, duration.ticks, amplifier, ambient, particles, icon)

fun LivingEntity.addPotionEffect(
    type: PotionEffectType,
    duration: Duration = infinitePotionDuration,
    amplifier: Int = 0,
    ambient: Boolean = false,
    particles: Boolean = false,
    icon: Boolean = false
) = this.addPotionEffect(potionEffect(type, duration, amplifier, ambient, particles, icon))

fun PotionMeta.addPotionEffect(
    type: PotionEffectType,
    duration: Duration = infinitePotionDuration,
    amplifier: Int = 0,
    ambient: Boolean = false,
    particles: Boolean = false,
    icon: Boolean = false,
    overwrite: Boolean = true
) = this.addCustomEffect(potionEffect(type, duration, amplifier, ambient, particles, icon), overwrite)