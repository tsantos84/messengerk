package com.messengerk.core

interface Middleware {
    fun handle(envelope: Envelope<Any>, stack: MiddlewareStack): Envelope<Any>
}