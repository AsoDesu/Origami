package dev.asodesu.origami.utilities

import net.kyori.adventure.audience.Audience

class DefaultDesignProvider : DesignProvider {
    override fun success(audience: Audience, str: String) = audience.send("<green>✔ $str")
    override fun warning(audience: Audience, str: String) = audience.send("<gold>⚠ $str")
    override fun info(audience: Audience, str: String) = audience.send("<yellow><b>⚡</b> $str")
    override fun error(audience: Audience, str: String) = audience.send("<red>⚠ $str")
}