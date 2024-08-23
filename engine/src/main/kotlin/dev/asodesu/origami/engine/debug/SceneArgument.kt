package dev.asodesu.origami.engine.debug

import cloud.commandframework.arguments.CommandArgument
import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.parsing.NoInputProvidedException
import dev.asodesu.origami.engine.scene.Scene
import dev.asodesu.origami.engine.scene.Scenes
import java.util.*
import org.bukkit.command.CommandSender

class SceneArgument(
    required: Boolean,
    name: String,
    defaultValue: String
) : CommandArgument<CommandSender, Scene>(
    required,
    name,
    SceneParser(),
    defaultValue,
    Scene::class.java,
    { ctx, _ -> Scenes.map.keys.toList() }
) {

    companion object {
        fun builder(name: String) = Builder(name)
        fun of(name: String) = builder(name).asRequired().build()
        fun optional(name: String) = builder(name).asOptional().build()

        class Builder(name: String) : CommandArgument.Builder<CommandSender, Scene>(Scene::class.java, name) {
            override fun build(): CommandArgument<CommandSender, Scene> {
                return SceneArgument(this.isRequired, this.name, this.defaultValue)
            }
        }
    }

    class SceneParser : ArgumentParser<CommandSender, Scene> {
        override fun parse(
            commandContext: CommandContext<CommandSender>,
            inputQueue: Queue<String>
        ): ArgumentParseResult<Scene> {
            val input = inputQueue.peek()
                ?: return ArgumentParseResult.failure(NoInputProvidedException(this::class.java, commandContext))
            val scene = Scenes.map[input]
                ?: return ArgumentParseResult.failure(IllegalArgumentException("No scene with id '$input'"))
            inputQueue.remove()
            return ArgumentParseResult.success(scene)
        }

    }

}