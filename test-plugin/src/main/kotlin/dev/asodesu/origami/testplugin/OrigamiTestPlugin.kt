package dev.asodesu.origami.testplugin

import cloud.commandframework.kotlin.extension.buildAndRegister
import dev.asodesu.origami.engine.Origami
import dev.asodesu.origami.testplugin.commands.applyBehaviourTests
import dev.asodesu.origami.utilities.commands.commandManager
import org.bukkit.plugin.java.JavaPlugin

class OrigamiTestPlugin : JavaPlugin() {

    override fun onEnable() {
        Origami.init(this)

        commandManager.buildAndRegister("test") {
            registerCopy("behaviour") { applyBehaviourTests() }
        }

        logger.info("Origami Test Plugin has loaded!")
    }

}