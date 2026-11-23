package com.example.jarvspromax.command

import org.junit.Assert.assertEquals
import org.junit.Test

class CommandRouterTest {

    @Test
    fun opensAllowlistedApp() {
        assertEquals(Command.OpenApp("settings"), CommandRouter.parse("open settings"))
    }

    @Test
    fun acceptsJarvisPrefixAndMixedCase() {
        assertEquals(Command.OpenApp("camera"), CommandRouter.parse("JARVIS, open Camera"))
    }

    @Test
    fun aliasResolvesToSameAppId() {
        assertEquals(Command.OpenApp("dialer"), CommandRouter.parse("open phone"))
    }

    @Test
    fun unlistedAppIsRejected() {
        assertEquals(Command.Unknown, CommandRouter.parse("open whatsapp"))
    }

    @Test
    fun shellLikeInputIsRejected() {
        assertEquals(Command.Unknown, CommandRouter.parse("open settings; rm -rf /"))
    }

    @Test
    fun emptyAndBlankInputAreRejected() {
        assertEquals(Command.Unknown, CommandRouter.parse(""))
        assertEquals(Command.Unknown, CommandRouter.parse("   "))
    }

    @Test
    fun overlongInputIsRejected() {
        assertEquals(Command.Unknown, CommandRouter.parse("open " + "a".repeat(200)))
    }

    @Test
    fun unknownVerbIsRejected() {
        assertEquals(Command.Unknown, CommandRouter.parse("delete settings"))
    }
}
