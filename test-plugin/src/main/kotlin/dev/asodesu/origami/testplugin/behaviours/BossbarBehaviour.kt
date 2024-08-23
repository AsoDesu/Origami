package dev.asodesu.origami.testplugin.behaviours

import dev.asodesu.origami.engine.player.PlayerBehaviour
import dev.asodesu.origami.engine.player.PlayerBehaviourContainer
import dev.asodesu.origami.engine.wiring.annotations.Subscribe
import dev.asodesu.origami.utilities.bukkit.debug
import dev.asodesu.origami.utilities.miniMessage
import net.kyori.adventure.bossbar.BossBar
import org.bukkit.event.player.PlayerJoinEvent

class BossbarBehaviour(val name: String, c: PlayerBehaviourContainer) : PlayerBehaviour(c) {
    val bossBar = BossBar.bossBar(miniMessage("BossbarBehaviour - <b>$name"), 1f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS)
    fun postApply() {
        debug("Applied behaviour to ${offlinePlayer.name} / $name")
        playerOrNull?.showBossBar(bossBar)
    }

    @Subscribe
    fun join(evt: PlayerJoinEvent) = player.showBossBar(bossBar)

    fun postRemove() {
        playerOrNull?.hideBossBar(bossBar)
    }

}