package com.messengerk.core

import com.messengerk.core.handler.MessageHandler
import com.messengerk.core.stamp.HandledStamp
import org.junit.jupiter.api.Test
import strikt.api.expectThat

internal class CommandBusTest {

    class FooMessage

    class FooHandler : MessageHandler<FooMessage> {
        override fun handle(envelope: Envelope<FooMessage>): Boolean {
            return true
        }
    }

    @Test
    fun `It should handle the message`() {
        val messageBus = MessageBusBuilder("foo").build {
            withHandler(FooHandler())
        }

        val envelope = messageBus.dispatch(FooMessage())
        expectThat(envelope).contains<HandledStamp>()
    }
}