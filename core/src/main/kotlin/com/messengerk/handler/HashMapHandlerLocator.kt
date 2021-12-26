package com.messengerk.handler

import com.messengerk.exception.MessageHandlerNotFoundException

class HashMapHandlerLocator(private val map: Map<String, MessageHandler<Any>> = emptyMap()) : HandlerLocator {
    override fun locate(name: String): MessageHandler<Any> {
        return map.getOrElse(name) {
            throw MessageHandlerNotFoundException("Handler with name \"$name\" was not found")
        }
    }
}