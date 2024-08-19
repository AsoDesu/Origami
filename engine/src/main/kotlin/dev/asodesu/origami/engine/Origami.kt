package dev.asodesu.origami.engine

import dev.asodesu.origami.engine.player.PlayerBehaviourRegistry
import dev.asodesu.origami.utilities.bukkit.BukkitUtilities
import dev.asodesu.origami.utilities.bukkit.register
import dev.asodesu.origami.utilities.commands.CloudCommandManager
import org.bukkit.plugin.java.JavaPlugin

object Origami {

    fun init(plugin: JavaPlugin) {
        CloudCommandManager.init(plugin)
        BukkitUtilities.init(plugin)

        PlayerBehaviourRegistry.register()
    }

}