package com.messengerk.spring_boot_starter

import com.messengerk.core.Envelope
import com.messengerk.core.MessageBus
import com.messengerk.core.handler.MessageHandler
import com.messengerk.core.stamp.HandledStamp
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import strikt.api.expectThat
import strikt.assertions.isTrue

internal class MessageBusAutoConfigurationTest {

    internal class FooMessage

    internal class FooHandler(private val commandBus: MessageBus) : MessageHandler<FooMessage> {
        override fun handle(envelope: Envelope<FooMessage>): Boolean {
            return true
        }
    }

    private lateinit var contextRunner: ApplicationContextRunner

    @BeforeEach
    fun setUp() {
        contextRunner = ApplicationContextRunner()
            .withPropertyValues("spring.application.name=testApp")
            .withConfiguration(AutoConfigurations.of(MessageBusConfiguration::class.java))
    }

    @Test
    fun `It should create a commandBus bean`() {
        contextRunner
            .withBean(FooHandler::class.java)
            .run {
                expectThat(it.containsBean("commandBus")).isTrue()
                val bus = it.getBean("commandBus") as MessageBus
                val envelope = bus.dispatch(FooMessage())
                expectThat(envelope.contains<HandledStamp>()).isTrue()
            }
    }
}