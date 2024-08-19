package dev.asodesu.origami.utilities

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

fun miniMessage(str: String) = MiniMessageProvider.impl.miniMessage(str)

fun Audience.send(str: String) {
    this.sendMessage(miniMessage(str))
}

fun Audience.success(str: String) = DesignProvider.impl.success(this, str)
fun Audience.warning(str: String) = DesignProvider.impl.warning(this, str)
fun Audience.info(str: String) = DesignProvider.impl.info(this, str)
fun Audience.error(str: String) = DesignProvider.impl.error(this, str)

fun times(fadeIn: Duration, stay: Duration, fadeOut: Duration) =
    Title.Times.times(fadeIn.toJavaDuration(), stay.toJavaDuration(), fadeOut.toJavaDuration())

fun Audience.sendTitle(
    title: String = "",
    subtitle: String = "",
    fadeIn: Duration = 0.1.seconds,
    stay: Duration = 2.seconds,
    fadeOut: Duration = 0.5.seconds
) {
    this.sendTitlePart(TitlePart.TIMES, times(fadeIn, stay, fadeOut))
    this.sendTitlePart(TitlePart.SUBTITLE, miniMessage(subtitle))
    this.sendTitlePart(TitlePart.TITLE, miniMessage(title))
}