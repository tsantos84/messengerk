package com.messengerk.core

data class Envelope<T> (
    val message: T,
    val stamps: MutableList<Stamp> = mutableListOf()
) {
    fun with(stamp: Stamp): Envelope<T> {
        stamps.add(stamp)
        return this
    }

    inline fun <reified T> contains(): Boolean {
        return allOf<T>().isNotEmpty()
    }

    inline fun <reified T> allOf(): List<Stamp> {
        return this.stamps.filter { T::class == it::class }
    }

    inline fun <reified T> firstOf(): T? {
        return allOf<T>().firstOrNull() as T?
    }

    inline fun <reified T> lastOf(): T? {
        return allOf<T>().lastOrNull() as T?
    }
}