package dev.asodesu.origami.utilities.commands

import cloud.commandframework.CommandManager
import org.bukkit.command.CommandSender

val commandManager: CommandManager<CommandSender>
    get() = CloudCommandManager.commandManager
        ?: throw IllegalAccessException("Cannot access global command manager as CloudCommandManager#init has not been completed!")