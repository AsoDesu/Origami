package dev.asodesu.origami.engine.debug

import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.standard.EnumArgument
import cloud.commandframework.bukkit.parsers.OfflinePlayerArgument
import cloud.commandframework.bukkit.parsers.PlayerArgument
import cloud.commandframework.context.CommandContext
import cloud.commandframework.kotlin.MutableCommandBuilder
import cloud.commandframework.kotlin.extension.commandBuilder
import dev.asodesu.origami.engine.Behaviour
import dev.asodesu.origami.engine.impl.BehaviourContainer
import dev.asodesu.origami.engine.scene.PlayerScene
import dev.asodesu.origami.engine.scene.Scene
import dev.asodesu.origami.engine.scene.Scenes
import dev.asodesu.origami.utilities.commands.commandManager
import dev.asodesu.origami.utilities.error
import dev.asodesu.origami.utilities.send
import dev.asodesu.origami.utilities.success
import java.util.*
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.jvm.optionals.getOrNull

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
            registerCopy("scenes") { registerSceneDebugs() }
        }
    }

    private fun MutableCommandBuilder<CommandSender>.registerSceneDebugs() {
        argument(SceneArgument.of("scene"))
        handler {
            val scene = it.get<Scene>("scene")
            it.sender.send(scene.getDebugInfo())
        }

        registerCopy("add") {
            argument(PlayerArgument.optional("player"))
            playerSceneHandler { playerScene, it ->
                val player = it.getOptional<Player>("player").getOrNull()
                    ?: it.sender as? Player
                    ?: return@playerSceneHandler it.sender.error("You must provide a player, or be a player.")
                playerScene.addPlayer(player)
                it.sender.success("Added ${player.name} to the scene")
            }
        }

        registerCopy("remove") {
            argument(
                OfflinePlayerArgument.builder<CommandSender?>("player")
                    .asOptional()
                    .withSuggestionsProvider { ctx, _ ->
                        val scene = ctx.get<Scene>("scene")
                        (scene as? PlayerScene<OfflinePlayer>)?.players?.map { it.name ?: it.uniqueId.toString() } ?: emptyList()
                    }
                    .withParser { _, inputQueue ->
                        val input = inputQueue.remove()
                        val player = if (input.length == 36) Bukkit.getOfflinePlayer(UUID.fromString(input))
                        else Bukkit.getOfflinePlayerIfCached(input)

                        if (player == null)
                            return@withParser ArgumentParseResult.failure(IllegalArgumentException("Player '$input' not found."))
                        ArgumentParseResult.success(player)
                    }
            )
            playerSceneHandler { playerScene, it ->
                val player = it.getOptional<OfflinePlayer>("player").getOrNull()
                    ?: it.sender as? OfflinePlayer
                    ?: return@playerSceneHandler it.sender.error("You must provide a player, or be a player.")
                playerScene.removePlayer(player)
                it.sender.success("Removed ${player.name} from the scene")
            }
        }

        registerCopy("destroy") {
            handler {
                val scene = it.get<Scene>("scene")
                Scenes.unregister(scene)
                it.sender.success("Unregistered scene!")
            }
        }
    }

    private fun MutableCommandBuilder<CommandSender>.playerSceneHandler(func: (PlayerScene<OfflinePlayer>, CommandContext<CommandSender>) -> Unit) {
        handler {
            val scene = it.get<Scene>("scene")
            val playerScene = scene as? PlayerScene<OfflinePlayer>
                ?: return@handler it.sender.error("That scene is not a player scene.")
            func(playerScene, it)
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