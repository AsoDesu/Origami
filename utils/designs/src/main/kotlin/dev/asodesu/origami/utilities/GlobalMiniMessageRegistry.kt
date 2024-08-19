package dev.asodesu.origami.utilities

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

object GlobalMiniMessageRegistry : MiniMessageProvider {
    private val tags = mutableListOf<TagResolver>()
    private var serializer = build()

    override fun miniMessage(str: String): Component {
        return serializer.deserialize(str)
    }

    fun addResolver(tagResolver: TagResolver) {
        tags += tagResolver
        serializer = build()
    }

    private fun build(): MiniMessage {
        return MiniMessage.builder()
            .tags(
                TagResolver.builder()
                    .resolver(TagResolver.standard())
                    .resolvers(tags)
                    .build()
            )
            .build()
    }
}