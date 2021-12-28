package com.messengerk.core.transport

import com.messengerk.core.config.TransportConfig

interface TransportFactory {
    fun create(config: TransportConfig): Transport
    fun supports(config: TransportConfig): Boolean
}