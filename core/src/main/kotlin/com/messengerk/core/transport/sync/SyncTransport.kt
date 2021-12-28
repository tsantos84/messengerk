package com.messengerk.core.transport.sync

import com.messengerk.core.Envelope
import com.messengerk.core.MessageBus
import com.messengerk.core.stamp.ReceivedStamp
import com.messengerk.core.transport.Transport

/**
 * Adds a ReceivedStamp to avoid the envelope to be sent twice
 */
class SyncTransport (private val messageBus: MessageBus) : Transport {
    override fun getName(): String = "sync"
    override fun send(envelope: Envelope<Any>): Envelope<Any> = messageBus.dispatch(envelope.with(ReceivedStamp()))
}