package com.messengerk.core

data class Envelope<T> (
    val message: T,
    val stamps: MutableList<Stamp> = mutableListOf()
) {
    fun addStamp(stamp: Stamp): Envelope<T> {
        stamps.add(stamp)
        return this
    }
}

inline fun <reified T> List<Stamp>.contains(): Boolean {
    return allOf<T>().isNotEmpty()
}

inline fun <reified T> List<Stamp>.allOf(): List<Stamp> {
    return this.filter { T::class == it::class }
}

inline fun <reified T> List<Stamp>.firstOf(): T? {
    return allOf<T>().firstOrNull() as T?
}

inline fun <reified T> List<Stamp>.lastOf(): T? {
    return allOf<T>().lastOrNull() as T?
}