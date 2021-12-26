package com.messengerk.core.middleware

import com.messengerk.core.Envelope
import com.messengerk.core.MiddlewareStack
import com.messengerk.core.contains
import com.messengerk.core.exception.MessageHandlerNotFoundException
import com.messengerk.core.firstOf
import com.messengerk.core.handler.MessageHandler
import com.messengerk.core.stamp.HandledStamp
import org.junit.jupiter.api.Test
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isSuccess
import strikt.assertions.isTrue

internal class HandleMiddlewareTest {

    class FooMessage

    @Test
    fun `It should handle a message`() {
        val handler = object : MessageHandler<FooMessage> {
            override fun handle(envelope: Envelope<FooMessage>): Boolean {
                return true
            }
        }

        val middleware = HandleMiddleware(
            mapOf(FooMessage::class to listOf(handler as MessageHandler<Any>))
        )

        val envelope = middleware.handle(Envelope(FooMessage()), MiddlewareStack(listOf(middleware)))
        expectThat(envelope).contains<HandledStamp>()

        val handledStamp = envelope.stamps.firstOf<HandledStamp>()!!
        expectThat(handledStamp.result!! as Boolean).isTrue()
    }

    @Test
    fun `It should throw an exception when there is no handler for the given message`() {
        val middleware = HandleMiddleware()
        expectThrows<MessageHandlerNotFoundException> {
            middleware.handle(Envelope(FooMessage()), MiddlewareStack(listOf(middleware)))
        }
    }

    @Test
    fun `It should not throw an exception when there is no handler for the given message`() {
        val middleware = HandleMiddleware(allowNoHandler = true)
        expectCatching {
            middleware.handle(Envelope(FooMessage()), MiddlewareStack())
        }.isSuccess()
    }
}