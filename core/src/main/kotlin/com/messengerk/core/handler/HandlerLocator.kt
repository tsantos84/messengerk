package com.messengerk.core.handler

interface HandlerLocator {
    fun locate(name: String): MessageHandler<Any>
}