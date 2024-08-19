package dev.asodesu.origami.utilities.bukkit

import dev.asodesu.origami.utilities.ticks
import dev.asodesu.origami.utilities.ticksLong
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import kotlin.time.Duration

private fun runnable(func: BukkitRunnable.() -> Unit) = object : BukkitRunnable(){
    override fun run() = func()
}

// thread
fun runMain(func: BukkitRunnable.() -> Unit) =
    runnable(func).runTask(plugin)
fun runAsync(func: BukkitRunnable.() -> Unit) =
    runnable(func).runTaskAsynchronously(plugin)

// delays
fun runLater(delay: Duration, func: BukkitRunnable.() -> Unit) =
    runnable(func).runTaskLater(plugin, delay.ticksLong)
fun runLaterAsync(delay: Duration, func: BukkitRunnable.() -> Unit) =
    runnable(func).runTaskLaterAsynchronously(plugin, delay.ticksLong)
fun nextTick(func: BukkitRunnable.() -> Unit) =
    runLater(1.ticks, func)

// repeating
fun runRepeating(period: Duration, delay: Duration = 0.ticks, func: BukkitRunnable.() -> Unit) =
    runnable(func).runTaskTimer(plugin, delay.ticksLong, period.ticksLong)
fun runRepeatingAsync(period: Duration, delay: Duration = 0.ticks, func: BukkitRunnable.() -> Unit) =
    runnable(func).runTaskTimerAsynchronously(plugin, delay.ticksLong, period.ticksLong)

fun loop(times: Int, period: Duration, func: Repeat.() -> Unit): BukkitTask {
    val repeat = Repeat(times)
    return runRepeating(period) {
        try {
            func(repeat)
        } catch (e: Exception) {
            Bukkit.getLogger().warning("Loop ${repeat.i} of task #$taskId threw an exception!")
            e.printStackTrace()
        }
        repeat.i++
        if (repeat.isLast) this.cancel()
    }
}
class Repeat(val total: Int) {
    var i: Int = 0
        internal set
    val remaining get() = total - (i+1)
    val isLast get() = remaining == 0
}