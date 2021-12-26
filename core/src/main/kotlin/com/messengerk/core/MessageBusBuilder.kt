package com.messengerk.core

import com.messengerk.core.handler.MessageHandler
import com.messengerk.core.middleware.AddBusNameMiddleware
import com.messengerk.core.middleware.HandleMiddleware
import com.messengerk.core.middleware.MessageHandlerLocator
import com.messengerk.core.middleware.SendMiddleware
import com.messengerk.core.transport.Routing
import com.messengerk.core.transport.Sender
import com.messengerk.core.transport.SenderRegistry
import kotlin.reflect.KClass

class MessageBusBuilder (busName: String) {

    private val handlerMap: MutableMap<KClass<*>, MutableList<MessageHandlerLocator>> = mutableMapOf()
    private val middlewares: MutableList<Middleware> = mutableListOf()
    private var allowNoHandler = false
    private val senders: MutableMap<String, Sender> = mutableMapOf()
    private val routes: MutableMap<KClass<*>, MutableList<String>> = mutableMapOf()

    init {
        withMiddleware(AddBusNameMiddleware(busName))
    }

    fun withHandler(handler: MessageHandler<*>): MessageBusBuilder {
        withHandler(handler::class) { handler as MessageHandler<Any>}
        return this
    }

    fun withHandler(handlerClass: KClass<*>, locator: MessageHandlerLocator): MessageBusBuilder {
        handlerClass.supertypes.forEach {
            if (it.classifier == MessageHandler::class) {
                val message = it.arguments.first().type!!.classifier as KClass<*>
                if (!handlerMap.containsKey(message)) {
                    handlerMap[message] = mutableListOf()
                }
                handlerMap[message]!!.add (locator)
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

    fun withSender(name: String, sender: Sender): MessageBusBuilder {
        senders[name] = sender
        return this
    }

    fun route(message: KClass<*>, senderName: String): MessageBusBuilder {
        if (!routes.containsKey(message)) {
            routes[message] = mutableListOf()
        }

        routes[message]!!.add(senderName)
        return this
    }

    fun build(action: MessageBusBuilder.() -> Unit): MessageBus {
        action(this)
        withMiddleware(SendMiddleware(
            senderRegistry = SenderRegistry(senders),
            routing = Routing(routes)
        ))
        withMiddleware(HandleMiddleware(handlerMap, allowNoHandler))
        return MessageBusImpl(middlewares)
    }
}