package com.messengerk.handler

import com.messengerk.handler.MessageHandler

interface HandlerLocator {
    fun locate(name: String): MessageHandler<Any>
}