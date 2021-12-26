package com.messengerk.core.transport

import com.messengerk.core.Envelope

interface Sender {
    fun send(envelope: Envelope<Any>): Envelope<Any>
}