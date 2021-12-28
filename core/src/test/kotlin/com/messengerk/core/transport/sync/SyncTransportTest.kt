package com.messengerk.core.transport.sync

import com.messengerk.core.*
import com.messengerk.core.handler.MessageHandler
import org.junit.jupiter.api.Test
import strikt.api.expectThat

internal class SyncTransportTest {

    internal class FooMessage

    @Test
    fun `It should add ReceivedStamp to envelope and dispatch the message`() {

        val handler = object : MessageHandler<FooMessage> {
            override fun handle(envelope: Envelope<FooMessage>): Boolean {
                return true
            }
        }

        val bus = MessageBusBuilder("commandBus").build {
            withHandler(handler)
        }

        val transport = SyncTransport(bus)
        val envelope = transport.send(Envelope(FooMessage()))
        expectThat(envelope).wasReceived()
        expectThat(envelope).wasHandled()
    }
}