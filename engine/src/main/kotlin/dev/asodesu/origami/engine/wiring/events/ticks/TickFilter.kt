package dev.asodesu.origami.engine.wiring.events.ticks

import dev.asodesu.origami.engine.wiring.annotations.Tick
import java.lang.reflect.Method

interface TickFilter {
    fun checkTick(method: Method, meta: Tick): Boolean
}