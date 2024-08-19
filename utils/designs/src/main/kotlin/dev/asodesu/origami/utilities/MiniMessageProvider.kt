package dev.asodesu.origami.utilities

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

interface MiniMessageProvider {
    fun miniMessage(str: String): Component

    companion object {
        internal var impl: MiniMessageProvider = GlobalMiniMessageRegistry
    }
}