package com.messengerk.core

import com.messengerk.core.exception.MessageBusNotFoundException
import com.messengerk.core.exception.UnroutableEnvelopeException
import com.messengerk.core.stamp.BusNameStamp

class RoutableMessageBus (private val busRegistry: MessageBusRegistry) : MessageBus {
    override fun dispatch(envelope: Envelope<Any>): Envelope<Any> {
        val stamp = envelope.lastOf<BusNameStamp>() ?: throw UnroutableEnvelopeException("Envelope is missing a BusNameStamp")
        val locator = busRegistry[stamp.name] ?: throw MessageBusNotFoundException(stamp.name)
        return locator().dispatch(envelope)
    }
}