package com.messengerk.handler

import com.messengerk.Envelope

interface MessageHandler<T> {
    fun handle(envelope: Envelope<T>): Any?
}