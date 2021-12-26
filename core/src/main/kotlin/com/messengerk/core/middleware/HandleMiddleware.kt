package com.messengerk.core.middleware

import com.messengerk.core.Envelope
import com.messengerk.core.handler.MessageHandler
import com.messengerk.core.Middleware
import com.messengerk.core.MiddlewareStack
import com.messengerk.core.exception.MessageHandlerNotFoundException
import com.messengerk.core.stamp.HandledStamp
import kotlin.reflect.KClass

class HandleMiddleware(
    private val handlers: Map<KClass<*>, List<MessageHandler<Any>>> = emptyMap(),
    private val allowNoHandler: Boolean = false
): Middleware {
    override fun handle(envelope: Envelope<Any>, stack: MiddlewareStack): Envelope<Any> {

        if (!handlers.containsKey(envelope.message::class)) {
            if (allowNoHandler) {
                return stack.next().handle(envelope, stack)
            }
            throw MessageHandlerNotFoundException("There is no message handler able to handle message of type " + envelope.message::class.qualifiedName)
        }

        this.handlers[envelope.message::class]!!.forEach {
            val result = it.handle(envelope)
            envelope.addStamp(HandledStamp(it::class.toString(), result))
        }

        return stack.next().handle(envelope, stack)
    }
}