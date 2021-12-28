package com.messengerk.core

import com.messengerk.core.stamp.HandledStamp
import com.messengerk.core.stamp.ReceivedStamp
import com.messengerk.core.stamp.SentStamp
import strikt.api.Assertion

inline fun <reified T> Assertion.Builder<Envelope<Any>>.contains(): Assertion.Builder<Envelope<Any>> {
    return assert("Envelope contains stamp of type" + T::class.toString()) {
        when(it.contains<T>()) {
            true -> pass()
            else -> fail()
        }
    }
}

fun Assertion.Builder<Envelope<Any>>.wasSent(): Assertion.Builder<Envelope<Any>> {
    return assert("Envelope was sent to transport") {
        when(it.contains<SentStamp>()) {
            true -> pass()
            else -> fail()
        }
    }
}

fun Assertion.Builder<Envelope<Any>>.wasReceived(): Assertion.Builder<Envelope<Any>> {
    return assert("Envelope was received") {
        when(it.contains<ReceivedStamp>()) {
            true -> pass()
            else -> fail()
        }
    }
}

fun Assertion.Builder<Envelope<Any>>.wasHandled(): Assertion.Builder<Envelope<Any>> {
    return assert("Envelope was handled") {
        when(it.contains<HandledStamp>()) {
            true -> pass()
            else -> fail()
        }
    }
}