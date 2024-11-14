@file:Suppress("unused")

package com.ramo.workoutly.global.util

fun <T> MutableList<T>.replace(predicate: (T) -> Boolean, invoke: (T) -> T): List<T> {
    val index = indexOfFirst(predicate)
    if (index == -1) {
        return this
    }
    this[index] = invoke(this[index])
    return this.toList()
}

fun <T> MutableList<T>.replace(predicate: (T) -> Boolean, invoke: (T) -> T, newInvoke: (List<T>, T?) -> Unit) {
    val index = indexOfFirst(predicate)
    if (index == -1) {
        newInvoke(this, null)
        return
    }
    invoke(this[index]).also {
        this[index] = it
    }.also {
        newInvoke(this.toList(), it)
    }
    return
}

fun Iterable<Double>.averageSafeDouble(): Double {
    if (sum() == 0.0) {
        return 0.0
    }
    return average()
}

fun Iterable<Long>.averageSafeLong(): Double {
    if (sum() == 0L) {
        return 0.0
    }
    return average()
}

inline fun Boolean.ifTrue(invoke: () -> Unit) {
    if (this) {
        invoke()
    }
}