package dev.asodesu.origami.utilities

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound

fun Audience.play(sound: Sound) = this.playSound(sound)
fun Audience.play(key: String, source: Sound.Source = Sound.Source.MASTER, volume: Float = 1f, pitch: Float = 1f)
    = this.playSound(sound(key, source, volume, pitch))
fun Audience.play(key: Key, source: Sound.Source = Sound.Source.MASTER, volume: Float = 1f, pitch: Float = 1f)
    = this.playSound(sound(key, source, volume, pitch))

fun sound(key: String, source: Sound.Source = Sound.Source.MASTER, volume: Float = 1f, pitch: Float = 1f)
    = Sound.sound(key(key), source, volume, pitch)
fun sound(key: Key, source: Sound.Source = Sound.Source.MASTER, volume: Float = 1f, pitch: Float = 1f)
    = Sound.sound(key, source, volume, pitch)

fun Sound.edit(
    key: Key? = null,
    source: Sound.Source? = null,
    volume: Float? = null,
    pitch: Float? = null,
) = sound(
    key = key ?: name(),
    source = source ?: source(),
    volume = volume ?: volume(),
    pitch = pitch ?: pitch()
)

fun Audience.play(
    sound: Sound,
    source: Sound.Source? = null,
    volume: Float? = null,
    pitch: Float? = null,
) = playSound(
    sound.edit(
        key = null,
        source,
        volume,
        pitch
    )
)