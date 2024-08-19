package dev.asodesu.origami.utilities

fun <T> nullableStackOf(vararg values: T) = NullableStackImpl(values.toMutableList())

class NullableStackImpl<T>(private val stack: MutableList<T> = mutableListOf()) : NullableStack<T> {

    override fun push(value: T) {
        stack.add(value)
    }
    override fun peek() = stack.lastOrNull()
    override fun pop() = stack.removeLastOrNull()

    override fun contains(value: T) = stack.contains(value)
}

interface NullableStack<T> {
    fun push(value: T)
    fun peek(): T?
    fun pop(): T?

    fun contains(value: T): Boolean
}