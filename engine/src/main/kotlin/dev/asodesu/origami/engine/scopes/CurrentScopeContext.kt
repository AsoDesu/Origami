package dev.asodesu.origami.engine.scopes


import dev.asodesu.origami.engine.Origami
import dev.asodesu.origami.engine.config.WarningLevel
import dev.asodesu.origami.engine.error.NoScopeInContextException
import dev.asodesu.origami.engine.logging.logger
import dev.asodesu.origami.utilities.NullableStack
import dev.asodesu.origami.utilities.nullableStackOf

object CurrentScopeContext : NullableStack<SceneScope> {
    private val threadLocal: ThreadLocal<NullableStack<SceneScope>> = ThreadLocal.withInitial { nullableStackOf() }

    override fun push(value: SceneScope) = threadLocal.get().push(value)
    override fun pop() = threadLocal.get().pop()
    override fun peek(): SceneScope {
        val get = threadLocal.get().peek()
        if (get == null) {
            when (Origami.config.scopeWarningLevel) {
                WarningLevel.WARN -> {
                    logger.warn("No scope in context, so the global scope was used. This may lead to resource leaks, " +
                            "if this was intended, use `scope(global) { <code> }` or register against the `global` scope.")
                }
                WarningLevel.EXCEPTION -> {
                    throw NoScopeInContextException("No scope in context.")
                }
                else -> {}
            }
            return Origami.config.scopeDefault
        }
        return get
    }

    override fun contains(value: SceneScope) = threadLocal.get().contains(value)
}