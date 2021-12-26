package com.messengerk.core.middleware

import com.messengerk.core.Envelope
import com.messengerk.core.MiddlewareStack
import com.messengerk.core.contains
import com.messengerk.core.stamp.ReceivedStamp
import com.messengerk.core.stamp.SentStamp
import com.messengerk.core.transport.Routing
import com.messengerk.core.transport.Sender
import com.messengerk.core.transport.SenderRegistry
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue

internal class SendMiddlewareTest {

    internal class FooMessage
    private val stack = MiddlewareStack()

    @Test
    fun `It should send the message if there is a route for that message and the message is not marked as received`() {
        var sent = false
        val sender = object : Sender {
            override fun send(envelope: Envelope<Any>): Envelope<Any> {
                sent = true
                return envelope
            }
        }
        val middleware = SendMiddleware(
            senderRegistry = SenderRegistry(mapOf("mock" to sender)),
            routing = Routing(mapOf(FooMessage::class to listOf("mock")))
        )
        val envelope = middleware.handle(Envelope(FooMessage()), stack)
        expectThat(envelope).contains<SentStamp>()
        expectThat(envelope.lastOf<SentStamp>()!!.sender).isEqualTo("mock")
        expectThat(sent).describedAs("sender sent the message").isTrue()
    }

    @Test
    fun `It should not send the message if there is a route for that message but the message is marked as received`() {
        var sent = false
        val sender = object : Sender {
            override fun send(envelope: Envelope<Any>): Envelope<Any> {
                sent = true
                return envelope
            }
        }
        val middleware = SendMiddleware(
            senderRegistry = SenderRegistry(mapOf("mock" to sender)),
            routing = Routing(mapOf(FooMessage::class to listOf("mock")))
        )
        val envelope = middleware.handle(Envelope(FooMessage(), mutableListOf(ReceivedStamp())), stack)
        expectThat(envelope).not().contains<SentStamp>()
        expectThat(sent).describedAs("sender sent the message").isFalse()
    }

    @Test
    fun `It should not send the message if there is no route for that message`() {
        var sent = false
        val sender = object : Sender {
            override fun send(envelope: Envelope<Any>): Envelope<Any> {
                sent = true
                return envelope
            }
        }
        val middleware = SendMiddleware(
            senderRegistry = SenderRegistry(mapOf("mock" to sender)),
            routing = Routing(mapOf())
        )
        val envelope = middleware.handle(Envelope(FooMessage()), stack)
        expectThat(envelope).not().contains<SentStamp>()
        expectThat(sent).describedAs("sender sent the message").isFalse()
    }
}