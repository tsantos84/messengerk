package com.messengerk.core.transport.sync

import com.messengerk.core.MessageBus
import com.messengerk.core.config.TransportConfig
import com.messengerk.core.transport.Transport
import com.messengerk.core.transport.TransportFactory

class SyncTransportFactory (private val messageBus: MessageBus) : TransportFactory {
    override fun create(config: TransportConfig): Transport = SyncTransport(messageBus)
    override fun supports(config: TransportConfig): Boolean = config.broker == "sync"
}