package com.messengerk

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isSameInstanceAs

internal class MiddlewareStackTest {

    class FooMiddleware : Middleware {
        override fun handle(envelope: Envelope<Any>, stack: MiddlewareStack): Envelope<Any> {
            return envelope
        }
    }

    @Test
    fun `It should be able to control current and next middleware in the stack`() {
        val middleware1 = FooMiddleware()
        val middleware2 = FooMiddleware()
        val stack = MiddlewareStack(listOf(middleware1, middleware2))
        expectThat(stack.current()).isSameInstanceAs(middleware1)
        expectThat(stack.next()).isSameInstanceAs(middleware2)
    }
}