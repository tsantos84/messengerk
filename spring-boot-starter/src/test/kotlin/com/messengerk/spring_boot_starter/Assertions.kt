package com.messengerk.spring_boot_starter

import com.messengerk.core.Envelope
import org.springframework.boot.test.context.assertj.AssertableApplicationContext
import org.springframework.context.ApplicationContext
import strikt.api.Assertion

inline fun <reified T> Assertion.Builder<Envelope<Any>>.contains(): Assertion.Builder<Envelope<Any>> =
    assert("Envelope contains stamp of type " + T::class.toString()) {
        when(it.contains<T>()) {
            true -> pass()
            else -> fail()
        }
    }

fun Assertion.Builder<AssertableApplicationContext>.containsBean(name: String): Assertion.Builder<AssertableApplicationContext> =
    assert("Application context has bean $name") {
        when(it.containsBean(name)) {
            true -> pass()
            else -> fail()
        }
    }