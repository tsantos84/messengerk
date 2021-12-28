package com.messengerk.core.config

import kotlin.reflect.KClass

class RoutingConfig (val message: KClass<*>) {
    val senders: MutableList<String> = mutableListOf()

    fun senders(vararg sender: String): RoutingConfig {
        this.senders.addAll(sender)
        return this
    }
}