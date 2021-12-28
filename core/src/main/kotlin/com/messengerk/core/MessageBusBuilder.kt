package com.messengerk.core

import com.messengerk.core.handler.MessageHandler
import com.messengerk.core.middleware.AddBusNameMiddleware
import com.messengerk.core.middleware.HandleMiddleware
import com.messengerk.core.middleware.MessageHandlerLocator
import com.messengerk.core.middleware.SendMiddleware
import com.messengerk.core.transport.Routing
import com.messengerk.core.transport.TransportRegistry
import kotlin.reflect.KClass

class MessageBusBuilder (private val busName: String) {

    private val handlerMap: MutableMap<KClass<*>, MutableList<MessageHandlerLocator>> = mutableMapOf()
    private val middlewares: MutableList<Middleware> = mutableListOf()
    private var allowNoHandler = false
    private var transportRegistry: TransportRegistry? = null
    private var routing: Routing? = null
    private val routes: MutableMap<KClass<*>, MutableList<String>> = mutableMapOf()

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

    fun withTransportRegistry(transportRegistry: TransportRegistry): MessageBusBuilder {
        this.transportRegistry = transportRegistry
        return this
    }

    fun withRouting(routing: Routing): MessageBusBuilder {
        this.routing = routing
        return this
    }

    fun build(action: MessageBusBuilder.() -> Unit): MessageBus {
        withMiddleware(AddBusNameMiddleware(busName))
        action(this)
        if (null != transportRegistry && null != routing) {
            withMiddleware(SendMiddleware(
                transportRegistry = transportRegistry!!,
                routing = routing!!
            ))
        }
        withMiddleware(HandleMiddleware(handlerMap, allowNoHandler))
        return MessageBusImpl(middlewares)
    }
}