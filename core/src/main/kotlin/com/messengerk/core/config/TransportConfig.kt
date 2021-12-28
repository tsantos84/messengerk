package com.messengerk.core.config

class TransportConfig(val name: String, val broker: String = name) {
    private val options: MutableMap<String, Any> = mutableMapOf()

    fun option(name: String, value: Any): TransportConfig {
        options[name] = value
        return this
    }

    fun option(options: Map<String, Any>) : TransportConfig {
        this.options.putAll(options)
        return this
    }
}