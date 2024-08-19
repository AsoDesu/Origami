package dev.asodesu.origami.utilities.commands

import cloud.commandframework.CommandManager
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.kotlin.extension.buildAndRegister
import cloud.commandframework.minecraft.extras.AudienceProvider
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler
import cloud.commandframework.paper.PaperCommandManager
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Function

object CloudCommandManager {
    internal var commandManager: CommandManager<CommandSender>? = null
        private set

    fun init(plugin: JavaPlugin) {
        val commandManager = PaperCommandManager(
            plugin,
            CommandExecutionCoordinator.simpleCoordinator(),
            Function.identity(),
            Function.identity()
        )
        MinecraftExceptionHandler<CommandSender>()
            .withArgumentParsingHandler()
            .withInvalidSyntaxHandler()
            .withInvalidSyntaxHandler()
            .withNoPermissionHandler()
            .withCommandExecutionHandler()
            .apply(commandManager, AudienceProvider.nativeAudience())
        this.commandManager = commandManager
    }

}