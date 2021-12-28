package com.messengerk.core.transport

import com.messengerk.core.Envelope
import com.messengerk.core.exception.NoSenderRoutedToMessageException
import kotlin.reflect.KClass

data class Routing(val routes: Map<KClass<*>, List<String>> = emptyMap()) {
    fun has(envelope: Envelope<*>): Boolean = routes.containsKey(envelope.message!!::class)
    fun get(envelope: Envelope<*>): List<String> = routes[envelope.message!!::class] ?: throw NoSenderRoutedToMessageException(envelope.message)
}
