package dev.asodesu.origami.objects.player

import dev.asodesu.origami.objects.GameObject
import org.bukkit.entity.Player

val Player.gameObject: GameObject get() = PlayerObjectRepository.getPlayer(this)