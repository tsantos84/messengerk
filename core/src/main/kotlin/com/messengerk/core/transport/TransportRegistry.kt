package com.messengerk.core.transport

import com.messengerk.core.exception.TransportNotFoundException

class TransportRegistry(private val transports: MutableMap<String, Transport> = mutableMapOf()) {

    fun register(transport: Transport) {
        transports[transport.getName()] = transport;
    }

    fun has(name: String): Boolean = transports.containsKey(name)

    fun get(name: String): Sender = transports.getOrElse(name) {
        throw TransportNotFoundException(name)
    }
}