package com.messengerk.core

class MessageBusImpl(private val middlewares: List<Middleware>) : MessageBus {
    override fun dispatch(envelope: Envelope<Any>): Envelope<Any> {
        val stack = MiddlewareStack(middlewares)
        stack.current().handle(envelope, stack)
        return envelope
    }
}