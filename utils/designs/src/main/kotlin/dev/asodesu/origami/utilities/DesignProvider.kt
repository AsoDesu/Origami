package dev.asodesu.origami.utilities

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component

interface DesignProvider {
    fun success(audience: Audience, str: String)
    fun warning(audience: Audience, str: String)
    fun info(audience: Audience, str: String)
    fun error(audience: Audience, str: String)

    companion object {
        internal var impl: DesignProvider = DefaultDesignProvider()
    }
}