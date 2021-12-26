package com.messengerk.core.handler

import com.messengerk.core.Envelope

interface MessageHandler<T> {
    fun handle(envelope: Envelope<T>): Any?
}