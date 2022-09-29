package io.github.landgrafhomyak.chatwars.wiki.html_compiler

import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class NameParserTest {
    @Test
    fun simpleName() = Parser.checkName("abc")

    @Test
    fun gravedName() = Parser.checkName("`a-b`")

    @Test
    fun startsWithDigit() {
        assertFailsWith(InvalidName::class) {
            Parser.checkName("0abc")
        }
    }

    @Test
    fun invalidCharInName() {
        assertFailsWith(InvalidName::class) {
            Parser.checkName("a-c")
        }
    }

    @Test
    fun graveInMiddleOfName() {
        assertFailsWith(InvalidName::class) {
            Parser.checkName("`a`b`")
        }
    }

    @Test
    fun unclosedGrave() {
        assertFailsWith(InvalidName::class) {
            Parser.checkName("`a")
        }
    }
}