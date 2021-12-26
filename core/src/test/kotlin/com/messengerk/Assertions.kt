package com.messengerk

import strikt.api.Assertion

inline fun <reified T> Assertion.Builder<Envelope<Any>>.contains(): Assertion.Builder<Envelope<Any>> {
    return assert("Envelope contains stamp of type" + T::class.toString()) {
        it.stamps.contains<T>()
    }
}