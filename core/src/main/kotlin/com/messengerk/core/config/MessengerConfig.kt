package com.messengerk.core.config

import kotlin.reflect.KClass

class MessengerConfig (action: (MessengerConfig.() -> Unit)? = null) {
    val buses: MutableList<BusConfig> = mutableListOf()
    val routing: MutableList<RoutingConfig> = mutableListOf()
    val transports: MutableList<TransportConfig> = mutableListOf()

    init {
        if (null != action) {
            action(this)
        }
    }

    operator fun invoke(action: MessengerConfig.() -> Unit): MessengerConfig {
        action(this)
        return this
    }

    fun bus(busName: String, action: BusConfig.() -> Unit): MessengerConfig {
        val config = BusConfig(busName)
        action(config)
        buses.add(config)
        return this
    }

    fun routing(clazz: KClass<*>, action: RoutingConfig.() -> Unit): MessengerConfig {
        val config = RoutingConfig(clazz)
        action(config)
        routing.add(config)
        return this
    }

    fun transport(name: String, action: (TransportConfig.() -> Unit)? = null): MessengerConfig {
        val config = TransportConfig(name)
        if (null !== action) {
            action(config)
        }
        transports.add(config)
        return this
    }
}
