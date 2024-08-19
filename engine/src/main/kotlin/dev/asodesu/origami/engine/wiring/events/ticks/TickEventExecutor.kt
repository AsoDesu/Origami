package dev.asodesu.origami.engine.wiring.events.ticks

import dev.asodesu.origami.engine.wiring.annotations.Tick
import java.lang.reflect.Method
import org.bukkit.scheduler.BukkitRunnable

class TickEventExecutor(private val instance: Any, val method: Method) : BukkitRunnable() {
    override fun run() {
        method.invoke(instance)
    }
}

class FilteredTickEventExecutor(
    private val filter: TickFilter,
    private val meta: Tick,
    private val delegate: TickEventExecutor
) : BukkitRunnable() {
    override fun run() {
        if (!filter.checkTick(delegate.method, meta)) return
        delegate.run()
    }
}