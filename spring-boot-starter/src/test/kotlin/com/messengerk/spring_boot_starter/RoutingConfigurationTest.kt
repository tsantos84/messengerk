package com.messengerk.spring_boot_starter

import com.messengerk.core.Envelope
import com.messengerk.core.config.MessengerConfig
import com.messengerk.core.transport.Routing
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

class RoutingConfigurationTest {

    internal class FooMessage

    internal class RoutingConfiguration (messengerConfig: MessengerConfig) {
        init {
            messengerConfig.routing(FooMessage::class) {
                senders("fooSender")
            }
        }
    }
    private lateinit var contextRunner: ApplicationContextRunner

    @BeforeEach
    fun setUp() {
        contextRunner = ApplicationContextRunner()
            .withUserConfiguration(RoutingConfiguration::class.java)
            .withConfiguration(AutoConfigurations.of(MessageBusConfiguration::class.java))
    }

    @Test
    fun `It should create the Routing bean`() {
        contextRunner.run {
            expectThat(it).containsBean("messengerRouting")
            val envelope = Envelope(FooMessage())
            val routing = it.getBean("messengerRouting") as Routing
            expectThat(routing.has(envelope)).describedAs("there is a route to the message ${FooMessage::class.qualifiedName} configured").isTrue()
            expectThat(routing.get(envelope)).isEqualTo(listOf("fooSender"))
        }
    }
}