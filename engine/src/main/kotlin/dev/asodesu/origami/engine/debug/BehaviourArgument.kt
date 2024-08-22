package dev.asodesu.origami.engine.debug

import cloud.commandframework.arguments.CommandArgument
import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.parsing.NoInputProvidedException
import dev.asodesu.origami.engine.Behaviour
import dev.asodesu.origami.engine.impl.BehaviourContainer
import java.util.*
import org.bukkit.command.CommandSender

class BehaviourArgument(
    required: Boolean,
    name: String,
    defaultValue: String
) : CommandArgument<CommandSender, Behaviour>(
    required,
    name,
    BehaviourContainerParser(),
    defaultValue,
    Behaviour::class.java,
    ::suggesions
) {

    companion object {
        fun builder(name: String) = Builder(name)
        fun of(name: String) = builder(name).asRequired().build()
        fun optional(name: String) = builder(name).asOptional().build()

        class Builder(name: String) : CommandArgument.Builder<CommandSender, Behaviour>(Behaviour::class.java, name) {
            override fun build(): CommandArgument<CommandSender, Behaviour> {
                return BehaviourArgument(this.isRequired, this.name, this.defaultValue)
            }
        }

        private fun suggesions(ctx: CommandContext<CommandSender>, input: String): List<String> {
            val container = ctx.getOrDefault<BehaviourContainer>("container", null) ?: return emptyList()
            return container.behaviours.map { it::class.java.simpleName }
        }
    }

    class BehaviourContainerParser : ArgumentParser<CommandSender, Behaviour> {
        override fun parse(
            commandContext: CommandContext<CommandSender>,
            inputQueue: Queue<String>
        ): ArgumentParseResult<Behaviour> {
            val input = inputQueue.peek()
                ?: return ArgumentParseResult.failure(NoInputProvidedException(this::class.java, commandContext))
            val container = commandContext.getOrDefault<BehaviourContainer>("container", null)
                ?: return ArgumentParseResult.failure(IllegalArgumentException("No Behaviour Container provided"))
            val behaviour = container.behaviours.find { it::class.java.simpleName == input }
                ?: return ArgumentParseResult.failure(IllegalStateException("This container does not have a behaviour for '$input'"))
            inputQueue.remove()
            return ArgumentParseResult.success(behaviour)
        }

    }

}