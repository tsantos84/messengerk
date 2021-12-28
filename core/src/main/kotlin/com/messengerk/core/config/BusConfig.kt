package com.messengerk.core.config

import com.messengerk.core.Middleware

class BusConfig(val name: String) {
    var allowNoHandler: Boolean = false
    private val middlewares: MutableList<Middleware> = mutableListOf()

    fun middleware(middleware: Middleware): BusConfig {
        middlewares.add(middleware)
        return this
    }
}