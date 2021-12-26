package com.messengerk.core

interface MessageBus {
    fun dispatch(message: Any, vararg stamp: Stamp): Envelope<Any> = dispatch(Envelope(message, stamp.toMutableList()))
    fun dispatch(envelope: Envelope<Any>): Envelope<Any>
}