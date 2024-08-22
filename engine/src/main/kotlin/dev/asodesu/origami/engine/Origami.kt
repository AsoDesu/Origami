package dev.asodesu.origami.engine

import dev.asodesu.origami.engine.config.OrigamiConfig
import dev.asodesu.origami.engine.debug.OrigamiCommand
import dev.asodesu.origami.engine.player.PlayerBehaviourRegistry
import dev.asodesu.origami.utilities.bukkit.BukkitUtilities
import dev.asodesu.origami.utilities.bukkit.register
import dev.asodesu.origami.utilities.commands.CloudCommandManager
import org.bukkit.plugin.java.JavaPlugin

object Origami {
    internal var config = OrigamiConfig()

    fun init(plugin: JavaPlugin) {
        CloudCommandManager.init(plugin)
        OrigamiCommand.register()
        BukkitUtilities.init(plugin)

        PlayerBehaviourRegistry.register()
    }

    fun configure(func: OrigamiConfig.() -> Unit) {
        func(config)
    }
}