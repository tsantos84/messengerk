package com.messengerk.spring_boot_starter

import com.messengerk.core.MessageBus
import com.messengerk.core.MessageBusBuilder
import com.messengerk.core.annotations.BusName
import com.messengerk.core.handler.MessageHandler
import com.messengerk.core.transport.Sender
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.reflect.KClass

@Configuration
open class MessageBusConfiguration: BeanDefinitionRegistryPostProcessor {

    private val handlerClasses: MutableMap<String, KClass<*>> = mutableMapOf()

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        val handlers = beanFactory.getBeanNamesForType(MessageHandler::class.java)
        handlers.forEach {
            val def = beanFactory.getBeanDefinition(it)
            handlerClasses[it] = Class.forName(def.beanClassName).kotlin
        }
    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
    }

    @Bean
    open fun commandBus(context: ApplicationContext): MessageBus {
        return factory("commandBus", false, context)
    }

    @Bean
    open fun eventBus(context: ApplicationContext): MessageBus {
        return factory("eventBus", true, context)
    }

    private fun factory(busName: String, allowNoHandler: Boolean, context: ApplicationContext): MessageBus {
        return MessageBusBuilder(busName).build {

            // register the handlers
            handlerClasses.forEach { it ->
                val busNameAnnotations = it.value.annotations.filterIsInstance<BusName>()
                if (busNameAnnotations.isEmpty() || busNameAnnotations.any { it.name == busName }) {
                    val handler = { context.getBean(it.key) as MessageHandler<Any> }
                    withHandler(it.value, handler)
                }
            }

            // register the senders
            context.getBeanNamesForType(Sender::class.java).forEach {
                withSender(it, context.getBean(it) as Sender)
            }

            // configure handle middleware
            allowNoHandler(allowNoHandler)
        }
    }
}