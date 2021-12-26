package com.messengerk.core.middleware

import com.messengerk.core.Envelope
import com.messengerk.core.MiddlewareStack
import com.messengerk.core.contains
import com.messengerk.core.handler.MessageHandler
import com.messengerk.core.stamp.BusNameStamp
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class AddBusNameMiddlewareTest {

    internal class FooMessage
    internal class FooHandler : MessageHandler<FooMessage> {
        override fun handle(envelope: Envelope<FooMessage>): Boolean {
            return true
        }
    }

    @Test
    fun `It should add the bus name as stamp to the envelope`() {
        val middleware = AddBusNameMiddleware("fooBus")
        val envelope = middleware.handle(Envelope(FooMessage()), MiddlewareStack())
        expectThat(envelope).contains<BusNameStamp>()
        expectThat(envelope.firstOf<BusNameStamp>()!!.name).isEqualTo("fooBus")
    }
}