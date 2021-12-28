package com.messengerk.core.middleware

import com.messengerk.core.Envelope
import com.messengerk.core.Middleware
import com.messengerk.core.MiddlewareStack
import com.messengerk.core.stamp.ReceivedStamp
import com.messengerk.core.stamp.SentStamp
import com.messengerk.core.transport.Routing
import com.messengerk.core.transport.TransportRegistry

class SendMiddleware (
    private val transportRegistry: TransportRegistry,
    private val routing: Routing
) : Middleware {
    override fun handle(envelope: Envelope<Any>, stack: MiddlewareStack): Envelope<Any> {

        if (envelope.contains<ReceivedStamp>() || !routing.has(envelope)) {
            return stack.next().handle(envelope, stack)
        }

        routing.get(envelope).forEach {
            val transport = transportRegistry.get(it)
            transport.send(envelope.with(SentStamp(it)))
        }

        return envelope;
    }
}