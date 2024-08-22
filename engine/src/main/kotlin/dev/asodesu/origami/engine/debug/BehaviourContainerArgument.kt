package dev.asodesu.origami.engine.debug

import cloud.commandframework.arguments.CommandArgument
import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.parsing.NoInputProvidedException
import dev.asodesu.origami.engine.impl.BehaviourContainer
import java.util.*
import org.bukkit.command.CommandSender

class BehaviourContainerArgument(
    required: Boolean,
    name: String,
    defaultValue: String
) : CommandArgument<CommandSender, BehaviourContainer>(
    required,
    name,
    BehaviourContainerParser(),
    defaultValue,
    BehaviourContainer::class.java,
    { ctx, _ -> ctx.getOrDefault<BehaviourType>("type", null)?.values?.invoke() ?: emptyList() }
) {

    companion object {
        fun builder(name: String) = Builder(name)
        fun of(name: String) = builder(name).asRequired().build()
        fun optional(name: String) = builder(name).asOptional().build()

        class Builder(name: String) : CommandArgument.Builder<CommandSender, BehaviourContainer>(BehaviourContainer::class.java, name) {
            override fun build(): CommandArgument<CommandSender, BehaviourContainer> {
                return BehaviourContainerArgument(this.isRequired, this.name, this.defaultValue)
            }
        }
    }

    class BehaviourContainerParser : ArgumentParser<CommandSender, BehaviourContainer> {
        override fun parse(
            commandContext: CommandContext<CommandSender>,
            inputQueue: Queue<String>
        ): ArgumentParseResult<BehaviourContainer> {
            val input = inputQueue.peek()
                ?: return ArgumentParseResult.failure(NoInputProvidedException(this::class.java, commandContext))
            val type = commandContext.get<BehaviourType>("type")
            val container = type.getter(input)
                ?: return ArgumentParseResult.failure(IllegalArgumentException("Can't get $type from input '$input'"))
            inputQueue.remove()
            return ArgumentParseResult.success(container)
        }

    }

}