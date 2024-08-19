package dev.asodesu.origami.engine.wiring.events.ticks

import dev.asodesu.origami.engine.error.InvalidPeriodicalException
import dev.asodesu.origami.engine.wiring.annotations.Tick
import dev.asodesu.origami.utilities.allMethods
import dev.asodesu.origami.utilities.bukkit.plugin
import java.lang.reflect.Method
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

typealias TickMethods = List<Pair<Method, Tick>>

object TickEvents {

    fun locateAndRegister(listener: Any, filter: TickFilter? = null): List<BukkitTask> {
        val tickMethods = locate(listener::class.java.allMethods)
        return register(listener, filter, tickMethods)
    }

    fun locate(methods: Iterable<Method>): TickMethods {
        return methods.mapNotNull {
            if (!it.isAnnotationPresent(Tick::class.java)) return@mapNotNull null
            val tick = it.getAnnotation(Tick::class.java)

            if (it.parameterCount > 0)
                throw InvalidPeriodicalException("Tick function must not take in any parameters")
            if (tick.ticks < 1)
                throw InvalidPeriodicalException("Cannot create tick function with a less-than-zero tick period.")
            it.trySetAccessible()
            it to tick
        }
    }

    fun register(instance: Any, filter: TickFilter? = null, tickMethods: TickMethods): List<BukkitTask> {
        return tickMethods.map { (method, meta) ->
            val executor = TickEventExecutor(instance, method)
            var runnable: BukkitRunnable = executor
            if (filter != null && !meta.ignoreFilter)
                runnable = FilteredTickEventExecutor(filter, meta, executor)

            runnable.runTaskTimer(plugin, 0L, meta.ticks.toLong())
        }
    }

}