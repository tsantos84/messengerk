package com.messengerk.core

import com.messengerk.core.handler.MessageHandler
import com.messengerk.core.middleware.HandleMiddleware
import kotlin.reflect.KClass

class MessageBusBuilder {

    private val handlerMap: MutableMap<KClass<*>, MutableList<MessageHandler<Any>>> = mutableMapOf()
    private val middlewares: MutableList<Middleware> = mutableListOf()
    private var allowNoHandler = false

    fun withHandler(handler: MessageHandler<*>): MessageBusBuilder {

        handler::class.supertypes.forEach {
            if (it.classifier == MessageHandler::class) {
                val message = it.arguments.first().type!!.classifier as KClass<*>
                if (!handlerMap.containsKey(message)) {
                    handlerMap[message] = mutableListOf()
                }
                handlerMap[message]!!.add(handler as MessageHandler<Any>)
            }
        }

        return this
    }

    fun withMiddleware(middleware: Middleware): MessageBusBuilder {
        middlewares.add(middleware)
        return this
    }

    fun allowNoHandler(allow: Boolean): MessageBusBuilder {
        allowNoHandler = allow
        return this
    }

    fun build(action: MessageBusBuilder.() -> Unit): MessageBus {
        action(this)
        middlewares.add(HandleMiddleware(handlerMap))
        return MessageBusImpl(middlewares)
    }
}