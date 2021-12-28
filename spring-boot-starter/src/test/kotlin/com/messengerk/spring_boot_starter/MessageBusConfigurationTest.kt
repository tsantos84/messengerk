package com.messengerk.spring_boot_starter

import com.messengerk.core.Envelope
import com.messengerk.core.MessageBus
import com.messengerk.core.MessageBusRegistry
import com.messengerk.core.annotations.BusName
import com.messengerk.core.handler.MessageHandler
import com.messengerk.core.stamp.BusNameStamp
import com.messengerk.core.stamp.HandledStamp
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

internal class MessageBusConfigurationTest {

    internal class FooMessage
    internal class BarMessage
    internal class BazMessage

    internal class FooHandler(private val commandBus: MessageBus) : MessageHandler<FooMessage> {
        override fun handle(envelope: Envelope<FooMessage>): Boolean {
            return true
        }
    }

    @BusName("commandBus")
    internal class BarHandler(private val commandBus: MessageBus) : MessageHandler<BarMessage> {
        override fun handle(envelope: Envelope<BarMessage>): Boolean {
            return true
        }
    }

    @BusName("eventBus")
    internal class BazHandler(private val commandBus: MessageBus) : MessageHandler<BazMessage> {
        override fun handle(envelope: Envelope<BazMessage>): Boolean {
            return true
        }
    }

    private lateinit var contextRunner: ApplicationContextRunner

    @BeforeEach
    fun setUp() {
        contextRunner = ApplicationContextRunner()
            .withBean(FooHandler::class.java)
            .withBean(BarHandler::class.java)
            .withBean(BazHandler::class.java)
            .withConfiguration(AutoConfigurations.of(MessageBusConfiguration::class.java))
    }

    @Test
    fun `It should create a commandBus bean`() {
        contextRunner
            .run {
                expectThat(it).containsBean("commandBus")
                val bus = it.getBean("commandBus") as MessageBus
                val envelope = bus.dispatch(FooMessage())
                expectThat(envelope).contains<HandledStamp>()
                expectThat(envelope).contains<BusNameStamp>()
                expectThat(envelope.firstOf<BusNameStamp>()!!.name).isEqualTo("commandBus")
            }
    }

    @Test
    fun `It should create an eventBus bean`() {
        contextRunner
            .run {
                expectThat(it).containsBean("eventBus")
                val bus = it.getBean("eventBus") as MessageBus
                val envelope = bus.dispatch(FooMessage())
                expectThat(envelope).contains<BusNameStamp>()
                expectThat(envelope.firstOf<BusNameStamp>()!!.name).isEqualTo("eventBus")
            }
    }

    @Test
    fun `It should register the MessageBusRegistry bean`() {
        contextRunner
            .run {
                expectThat(it).containsBean("messengerMessageBusRegistry")
                val registry = it.getBean("messengerMessageBusRegistry") as MessageBusRegistry
                expectThat(registry.containsKey("eventBus")).describedAs("registry contains the bus eventBus").isTrue()
                expectThat(registry["eventBus"]!!.invoke()).isA<MessageBus>()
            }
    }
}