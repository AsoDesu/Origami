package dev.asodesu.origami.engine.scopes

val global get() = Scope.global

fun scope(scope: Scope, func: () -> Unit) {
    scope.use(func)
}