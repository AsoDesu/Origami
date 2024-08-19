package dev.asodesu.origami.engine.player

import dev.asodesu.origami.engine.wiring.annotations.Tick
import dev.asodesu.origami.engine.wiring.events.ticks.TickFilter
import java.lang.reflect.Method

abstract class OnlinePlayerBehaviour(c: PlayerBehaviourContainer) : PlayerBehaviour(c), TickFilter {
    override fun checkTick(method: Method, meta: Tick) = isOnline
}