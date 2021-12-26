package com.messengerk

class MiddlewareStack(private val middlewares: List<Middleware> = emptyList()) {

    private var index: Int = 0

    fun current(): Middleware = middlewares.getOrElse(index) {
        return object : Middleware {
            override fun handle(envelope: Envelope<Any>, stack: MiddlewareStack): Envelope<Any> {
                return envelope
            }
        }
    }

    fun next(): Middleware {
        ++index
        return current()
    }
}