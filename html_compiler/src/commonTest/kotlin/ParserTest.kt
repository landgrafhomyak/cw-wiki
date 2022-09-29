package io.github.landgrafhomyak.chatwars.wiki.html_compiler

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith

internal class ParserTest {
    interface TestBuilder {
        infix fun plain(html: () -> String): TestBuilder
        infix fun inline(kt: () -> String): TestBuilder
        infix fun block(kt: () -> String): TestBuilder
        infix fun property(def: () -> Pair<String, String>): TestBuilder
    }

    private fun test(test: TestBuilder.() -> Unit) {
        val expectedProperties = ArrayList<Property>()
        val expectedEntities = ArrayList<Entity>()
        val source = StringBuilder()

        class EntitiesConsumer : TestBuilder {
            override fun plain(html: () -> String): TestBuilder {
                val str = html()
                if (Parser.injectionStartPattern.find(str) != null)
                    throw IllegalArgumentException("Injection start sequence <% in plain text")
                expectedEntities.add(Entity.Plain(source.length.toUInt(), str.length.toUInt()))
                source.append(str)
                return this
            }

            override fun inline(kt: () -> String): TestBuilder {
                val str = kt()
                if (Parser.injectionEndPattern.find(str) != null)
                    throw IllegalArgumentException("Injection closing sequence %> in injection")
                source.append("<%=")
                expectedEntities.add(Entity.KotlinInlineInjection(source.length.toUInt(), str.length.toUInt()))
                source.append(str)
                source.append("%>")
                return this
            }

            override fun block(kt: () -> String): TestBuilder {
                val str = kt()
                if (Parser.injectionEndPattern.find(str) != null)
                    throw IllegalArgumentException("Injection closing sequence %> in injection")
                source.append("<%")
                expectedEntities.add(Entity.KotlinBlockInjection(source.length.toUInt(), str.length.toUInt()))
                source.append(str)
                source.append("%>")
                return this
            }

            override fun property(def: () -> Pair<String, String>): TestBuilder {
                val (name, type) = def()
                if (Parser.injectionEndPattern.find(name) != null)
                    throw IllegalArgumentException("Injection closing sequence %> in property name")
                if (name.find { c -> c == ':' } != null)
                    throw IllegalArgumentException("Injection closing sequence %> in property name")
                if (Parser.injectionEndPattern.find(type) != null)
                    throw IllegalArgumentException("Type separator in property name")
                expectedProperties.add(Property(name.trim(), type.trim()))
                source.append("<%@")
                source.append(name)
                source.append(":")
                source.append(type)
                source.append("%>")
                return this
            }
        }
        test(EntitiesConsumer())
        val (actualProperties, actualEntities) = Parser.parse(source)
        assertContentEquals(expectedProperties, actualProperties)
        assertContentEquals(expectedEntities, actualEntities)
    }


    private inline fun <reified E : Throwable> testCatching(noinline invalidTest: TestBuilder.() -> Unit) {
        var iae: IllegalArgumentException? = null
        assertFailsWith(E::class) {
            try {
                test(invalidTest)
            } catch (t: IllegalArgumentException) {
                iae = t
            }
        }
        throw iae ?: return
    }

    @Test
    fun testPlainHtml() = test {
        plain { "<html></html>" }
    }

    @Test
    fun testOneInlineInjection() = test {
        inline { "123" }
    }

    @Test
    fun testOneBlockInjection() = test {
        block { "println(123)" }
    }

    @Test
    fun testUnclosedInjection() {
        assertFailsWith(UnclosedInjection::class) { Parser.parse("<% 123413241234123412") }
    }

    @Test
    fun testPlainAndInjection() = test {
        plain { "<title>" } inline { "123" } plain { "</title>" }
    }

    @Test
    fun testPlainAnd2Injections() = test {
        plain { "<title>" } inline { "123" } inline { "321" } plain { "</title>" }
    }

    @Test
    fun testOneProperty() = test {
        property { "name" to "Type" }
    }

    @Test
    fun testTwoProperty() = test {
        property { "name1" to "Type" }
        property { "name2" to "Type" }
    }

    @Test
    fun testTwoPropertiesSeparatedByNewLine() = test {
        property { "name1" to "Type" } plain { "\n" }
        property { "name2" to "Type" }
    }

    @Test
    fun testTwoPropertiesSeparatedByText() = testCatching<PropertyNotOnTop> {
        property { "name1" to "Type" } plain { "aboba" }
        property { "name2" to "Type" }
    }

    @Test
    fun testTwoPropertiesSeparatedByEmptyInjection() = testCatching<PropertyNotOnTop> {
        property { "name1" to "Type" } inline { "" }
        property { "name2" to "Type" }
    }
}