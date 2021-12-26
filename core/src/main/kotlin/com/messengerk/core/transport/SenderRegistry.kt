package com.messengerk.core.transport

import com.messengerk.core.exception.SenderNotFoundException

class SenderRegistry(private val senders: Map<String, Sender> = emptyMap()) {
    fun has(name: String): Boolean = senders.containsKey(name)

    fun get(name: String): Sender = senders.getOrElse(name) {
        throw SenderNotFoundException(name)
    }
}