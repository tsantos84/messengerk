package com.messengerk.middleware

import com.messengerk.Envelope
import com.messengerk.handler.MessageHandler
import com.messengerk.exception.MessageHandlerNotFoundException
import com.messengerk.handler.HashMapHandlerLocator
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