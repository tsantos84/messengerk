package com.messengerk

interface Middleware {
    fun handle(envelope: Envelope<Any>, stack: MiddlewareStack): Envelope<Any>
}