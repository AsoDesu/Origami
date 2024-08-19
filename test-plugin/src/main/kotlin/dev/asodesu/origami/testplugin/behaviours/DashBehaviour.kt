package dev.asodesu.origami.testplugin.behaviours

import dev.asodesu.origami.engine.player.OnlinePlayerBehaviour
import dev.asodesu.origami.engine.player.PlayerBehaviourContainer
import dev.asodesu.origami.engine.wiring.annotations.Subscribe
import dev.asodesu.origami.engine.wiring.annotations.Tick
import dev.asodesu.origami.utilities.Interpolation
import dev.asodesu.origami.utilities.bukkit.locationLerp
import dev.asodesu.origami.utilities.bukkit.loop
import dev.asodesu.origami.utilities.bukkit.off
import dev.asodesu.origami.utilities.bukkit.particle
import dev.asodesu.origami.utilities.play
import dev.asodesu.origami.utilities.ticks
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import kotlin.math.min
import kotlin.random.Random

class DashBehaviour(c: PlayerBehaviourContainer,  val maxDashInventory: Int = 2) : OnlinePlayerBehaviour(c) {
    var dashInventory = 2
        set(value) {
            val was = field
            field = value
            updateDashCounter(was)
        }

    private var isDashing = false
    private val canDash get() = dashInventory > 0

    fun updateDashCounter(was: Int) {
        val target = min(dashInventory / maxDashInventory.toFloat(), 0.99999f)
        player.level = dashInventory

        if (dashInventory < was)
            expInterpolation = Interpolation.float(player.exp, target, 4.ticks)
        else {
            player.exp = target
            expInterpolation = null
        }
    }

    var expInterpolation: Interpolation<Float>? = null

    @Tick
    private fun tick() {
        if (expInterpolation != null) {
            player.exp = expInterpolation!!.tick()
            if (expInterpolation?.isFinished == true) expInterpolation = null
        }

        if (dashInventory >= maxDashInventory) return
        if (player.isOnGround) {
            dashInventory = maxDashInventory
            isDashing = false
        }
    }

    @Subscribe
    private fun jump(evt: PlayerSwapHandItemsEvent) {
        evt.isCancelled = true

        if (!canDash) return
        player.velocity = player.location.direction.multiply(2.0)
        isDashing = true
        dashInventory--

        val pitch = Random.nextDouble(0.8, 1.0).toFloat()
        player.play("entity.wind_charge.wind_burst", pitch = pitch)
        player.world.particle(Particle.POOF, player.location, count = 5, extra = 0.05)

        var lastPlayerLocation = player.location
        var playerLocation = player.location
        val resolution = 5
        loop(5, 1.ticks) {
            if (i > 1) {
                for (i in 0..resolution) {
                    val t = i / resolution.toDouble()
                    val interpolatedLocation = locationLerp(lastPlayerLocation, playerLocation, t)

                    val location = interpolatedLocation.off(
                        x = Random.nextDouble(-0.05, 0.05),
                        y = 0.1 + Random.nextDouble(0.0, 1.8),
                        z = Random.nextDouble(-0.05, 0.05),
                    )
                    player.world.particle(
                        Particle.DUST, location,
                        extra = 0.0,
                        data = DustOptions(Color.WHITE, 1.5f)
                    )
                }
            }
            lastPlayerLocation = playerLocation
            playerLocation = player.location
        }
    }

    @Subscribe
    private fun fallDamage(evt: EntityDamageEvent) {
        if (evt.cause != EntityDamageEvent.DamageCause.FALL) return
        if (isDashing) evt.isCancelled = true
    }
}