package com.messengerk.spring_boot_starter

import com.messengerk.core.MessageBus
import com.messengerk.core.MessageBusBuilder
import com.messengerk.core.annotations.BusName
import com.messengerk.core.config.BusConfig
import com.messengerk.core.config.MessengerConfig
import com.messengerk.core.handler.MessageHandler
import com.messengerk.core.transport.*
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.reflect.KClass

@Configuration
open class MessageBusConfiguration: BeanDefinitionRegistryPostProcessor {

    @Bean
    open fun messengerRouting(config: MessengerConfig): Routing {
        val routing: MutableMap<KClass<*>, List<String>> = mutableMapOf()

        config.routing.forEach {
            routing[it.message] = it.senders
        }

        return Routing(routing)
    }

    @Bean
    open fun messengerTransportRegistry(context: ApplicationContext, config: MessengerConfig): TransportRegistry {

        val transportRegistry = TransportRegistry()

        val factories = context.getBeanNamesForType(TransportFactory::class.java).map {
            context.getBean(it) as TransportFactory
        }

        config.transports.forEach { transportConfig ->
            factories.forEach {
                if (it.supports(transportConfig)) {
                    transportRegistry.register(it.create(transportConfig))
                }
            }
        }

        return transportRegistry
    }

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        val handlers = beanFactory.getBeanNamesForType(MessageHandler::class.java)
        val handlerClasses: MutableMap<String, KClass<*>> = mutableMapOf()

        handlers.forEach {
            val def = beanFactory.getBeanDefinition(it)
            handlerClasses[it] = Class.forName(def.beanClassName).kotlin
        }

        if (!beanFactory.containsBean("messengerConfig")) {
            beanFactory.registerSingleton("messengerConfig", messengerConfig())
        }

        val config = beanFactory.getBean("messengerConfig") as MessengerConfig

        config.buses.forEach {
            val bus = factory(it, handlerClasses, beanFactory)
            beanFactory.registerSingleton(it.name, bus)
        }
    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
    }

    private fun messengerConfig(): MessengerConfig = MessengerConfig {
        bus("commandBus") {
            allowNoHandler = false
        }

        bus("eventBus") {
            allowNoHandler = true
        }
    }

    private fun factory(busConfig: BusConfig, handlerClasses: Map<String, KClass<*>>, context: ConfigurableListableBeanFactory): MessageBus {
        return MessageBusBuilder(busConfig.name).build {

            // register the handlers
            handlerClasses.forEach { it ->
                val busNameAnnotations = it.value.annotations.filterIsInstance<BusName>()
                if (busNameAnnotations.isEmpty() || busNameAnnotations.any { it.name == busConfig.name }) {
                    val handler = { context.getBean(it.key) as MessageHandler<Any> }
                    withHandler(it.value, handler)
                }
            }

            // register the transports
            withTransportRegistry(context.getBean("messengerTransportRegistry") as TransportRegistry)

            // register the routing
            withRouting(context.getBean("messengerRouting") as Routing)

            // configure handle middleware
            allowNoHandler(busConfig.allowNoHandler)
        }
    }
}