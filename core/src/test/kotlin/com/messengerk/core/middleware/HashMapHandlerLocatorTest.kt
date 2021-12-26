package com.messengerk.core.middleware

import com.messengerk.core.Envelope
import com.messengerk.core.handler.MessageHandler
import com.messengerk.core.exception.MessageHandlerNotFoundException
import com.messengerk.core.handler.HashMapHandlerLocator
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isSameInstanceAs

internal class HashMapHandlerLocatorTest {

    @Test
    fun `It should be able to locate a handler`() {
        val handler = object : MessageHandler<HandleMiddlewareTest.FooMessage> {
            override fun handle(envelope: Envelope<HandleMiddlewareTest.FooMessage>): Boolean {
                return true
            }
        }

        val locator = HashMapHandlerLocator(mapOf("foo" to (handler as MessageHandler<Any>)))

        expectThat(locator.locate("foo")).isSameInstanceAs(handler)
    }

    @Test
    fun `It should throw an exception when the handler does not exist`() {
        val locator = HashMapHandlerLocator()
        expectThrows<MessageHandlerNotFoundException> {
            locator.locate("unknown")
        }
    }
}