package com.messengerk.core.middleware

import com.messengerk.core.Envelope
import com.messengerk.core.Middleware
import com.messengerk.core.MiddlewareStack
import com.messengerk.core.stamp.BusNameStamp

class AddBusNameMiddleware (private val busName: String) : Middleware {
    override fun handle(envelope: Envelope<Any>, stack: MiddlewareStack): Envelope<Any> {
        envelope.with(BusNameStamp(busName))
        return stack.next().handle(envelope, stack)
    }
}