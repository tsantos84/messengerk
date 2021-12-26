package com.messengerk.core

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.*

internal class EnvelopeTest {

    class FooMessage
    class FooStamp : Stamp
    class BarStamp : Stamp

    @Test
    fun `It should be able to add a stamp`() {
        val envelope = Envelope(FooMessage())
        expectThat(envelope.stamps).hasSize(0)
        envelope.addStamp(FooStamp())
        expectThat(envelope.stamps).hasSize(1)
    }
    
    @Test
    fun `It should return all stamps of the given type`() {
        val envelope = Envelope(FooMessage(), mutableListOf(FooStamp(), BarStamp()))
        val fooStamps = envelope.stamps.allOf<BarStamp>()
        expectThat(fooStamps).hasSize(1)
    }

    @Test
    fun `It should be able to verify whether the envelope has a stamp of the given type or not`() {
        val envelope = Envelope(FooMessage())
        expectThat(envelope.stamps.contains<FooStamp>()).isFalse()
        envelope.addStamp(FooStamp())
        expectThat(envelope.stamps.contains<FooStamp>()).isTrue()
    }

    @Test
    fun `It should be able to return the first stamp of the given type`() {
        val envelope = Envelope(FooMessage())
        val stamp = FooStamp()
        envelope.addStamp(stamp)
        expectThat(envelope.stamps.firstOf<FooStamp>()).isSameInstanceAs(stamp)
    }

    @Test
    fun `It should return null if there is no stamp with the given type`() {
        val envelope = Envelope(FooMessage())
        expectThat(envelope.stamps.firstOf<FooStamp>()).isNull()
        expectThat(envelope.stamps.lastOf<FooStamp>()).isNull()
    }

    @Test
    fun `It should be able to return the last stamp of the given type`() {
        val envelope = Envelope(FooMessage())
        val foo1Stamp = FooStamp()
        val foo2Stamp = FooStamp()
        envelope.addStamp(foo1Stamp)
        envelope.addStamp(foo2Stamp)
        expectThat(envelope.stamps.lastOf<FooStamp>()).isSameInstanceAs(foo2Stamp)
    }
}