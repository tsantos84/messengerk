package com.messengerk.spring_boot_starter

import com.messengerk.core.Envelope
import com.messengerk.core.config.MessengerConfig
import com.messengerk.core.config.TransportConfig
import com.messengerk.core.transport.Transport
import com.messengerk.core.transport.TransportFactory
import com.messengerk.core.transport.TransportRegistry
import com.messengerk.core.transport.sync.SyncTransportFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.context.annotation.Bean
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isTrue

internal class TransportConfigurationTest {

    internal class MockTransportFactory : TransportFactory {
        override fun create(config: TransportConfig): Transport {
            return MockTransport()
        }

        override fun supports(config: TransportConfig): Boolean = config.name == "mock"
    }
    internal class MockTransport : Transport {
        override fun send(envelope: Envelope<Any>): Envelope<Any> {
            return envelope
        }

        override fun getName(): String = "mock"
    }
    internal class TransportConfiguration {
        @Bean
        fun messengerConfig(): MessengerConfig = MessengerConfig {
            transport("mock")
        }
    }
    private lateinit var contextRunner: ApplicationContextRunner

    @BeforeEach
    fun setUp() {
        contextRunner = ApplicationContextRunner()
            .withUserConfiguration(TransportConfiguration::class.java)
            .withConfiguration(AutoConfigurations.of(MessageBusConfiguration::class.java))
    }

    @Test
    fun `It should create the TransportRegistry bean`() {
        contextRunner
            .withBean(MockTransportFactory::class.java)
            .run {
                expectThat(it).containsBean("messengerTransportRegistry")
                val registry = it.getBean("messengerTransportRegistry") as TransportRegistry
                expectThat(registry.has("mock")).describedAs("transport registry contains transport \"mock\"").isTrue()
            }
    }

    @Test
    fun `It should create register SyncTransportFactory`() {
        contextRunner
            .run {
                expectThat(it).containsBean("messengerTransportSyncFactory")
                val transport = it.getBean("messengerTransportSyncFactory") as TransportFactory
                expectThat(transport).isA<SyncTransportFactory>()
            }
    }
}