package dev.asodesu.origami.engine.debug

import cloud.commandframework.arguments.standard.EnumArgument
import cloud.commandframework.kotlin.MutableCommandBuilder
import cloud.commandframework.kotlin.extension.commandBuilder
import dev.asodesu.origami.engine.Behaviour
import dev.asodesu.origami.engine.impl.BehaviourContainer
import dev.asodesu.origami.utilities.commands.commandManager
import dev.asodesu.origami.utilities.send
import org.bukkit.command.CommandSender

internal object OrigamiCommand {
    private const val GITHUB_LINK =
        "<hover:show_text:'Click to visit <origami>github.com'><click:open_url:'https://github.com/AsoDesu/Origami'>" +
                "github.com/AsoDesu/Origami" +
                "</click></hover>"

    fun register() {
        command.register()
    }

    private val command = commandManager.commandBuilder("origami") {
        handler {
            it.sender.send("""
                <gray><origami>[<b>🦋</b>]</origami> This server is built with <origami>Origami</origami>!
                <origami>[<b>⚡</b>]</origami> Version: <origami>unknown</origami>
                <origami>[✎]</origami> GitHub: <origami>$GITHUB_LINK</origami>
            """.trimIndent())
        }

        registerCopy("debug") {
            permission("origami.debugger")
            registerCopy("behaviours") { registerBehaviourDebugs() }
        }
    }

    private fun MutableCommandBuilder<CommandSender>.registerBehaviourDebugs() {
        behaviourContainerArgument()
        handler {
            val container = it.get<BehaviourContainer>("container")
            it.sender.send("\n<gray>" + container.getDebugInfo())
        }
        registerCopy {
            argument(BehaviourArgument.of("behaviour"))
            handler {
                val behaviour = it.get<Behaviour>("behaviour")
                it.sender.send("\n<gray>" + behaviour.getDebugInfo())
            }
        }
    }

    private fun MutableCommandBuilder<CommandSender>.behaviourContainerArgument() {
        argument(EnumArgument.of(BehaviourType::class.java, "type"))
        argument(BehaviourContainerArgument.of("container"))
    }

}