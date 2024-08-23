package dev.asodesu.origami.engine.scopes

val global get() = SceneScope.global

fun scope(scope: SceneScope, func: () -> Unit) {
    scope.use(func)
}