package com.messengerk.spring_boot_starter

import com.messengerk.core.MessageBus
import com.messengerk.core.MessageBusBuilder
import com.messengerk.core.handler.MessageHandler
import com.messengerk.core.middleware.MessageHandlerLocator
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.reflect.KClass

@Configuration
open class MessageBusConfiguration: BeanDefinitionRegistryPostProcessor {

    private val handlerTypes: MutableMap<String, KClass<*>> = mutableMapOf()

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        val handlers = beanFactory.getBeanNamesForType(MessageHandler::class.java)
        handlers.forEach {
            val def = beanFactory.getBeanDefinition(it)
            handlerTypes[it] = Class.forName(def.beanClassName).kotlin
        }
    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
    }

    @Bean
    open fun commandBus(context: ApplicationContext): MessageBus {
        return MessageBusBuilder().build {
            handlerTypes.forEach {
                val handler = { context.getBean(it.key) as MessageHandler<Any> }
                withHandler(it.value, handler)
            }

            allowNoHandler(false)
        }
    }
}