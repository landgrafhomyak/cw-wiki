package io.github.landgrafhomyak.chatwars.wiki.html_compiler

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith

internal class ParserTest {
    interface TestBuilder {
        infix fun plain(html: () -> String): TestBuilder
        infix fun inline(kt: () -> String): TestBuilder
        infix fun block(kt: () -> String): TestBuilder
        infix fun property(def: () -> Pair<String, Type>): TestBuilder
        infix fun import(def: () -> String): TestBuilder
    }

    private fun test(test: TestBuilder.() -> Unit) {
        val expectedProperties = ArrayList<Property>()
        val expectedEntities = ArrayList<Entity>()
        val expectedImports = ArrayList<Import>()
        val source = StringBuilder()

        class EntitiesConsumer : TestBuilder {
            override fun plain(html: () -> String): TestBuilder {
                val str = html()
                if (Parser.injectionStartPattern.find(str) != null)
                    throw IllegalArgumentException("Injection start sequence <% in plain text")
                expectedEntities.add(Entity.Plain(source.length, str.length))
                source.append(str)
                return this
            }

            override fun inline(kt: () -> String): TestBuilder {
                val str = kt()
                if (Parser.injectionEndPattern.find(str) != null)
                    throw IllegalArgumentException("Injection closing sequence %> in injection")
                source.append("<%=")
                expectedEntities.add(Entity.KotlinInlineInjection(source.length, str.length))
                source.append(str)
                source.append("%>")
                return this
            }

            override fun block(kt: () -> String): TestBuilder {
                val str = kt()
                if (Parser.injectionEndPattern.find(str) != null)
                    throw IllegalArgumentException("Injection closing sequence %> in injection")
                source.append("<%")
                expectedEntities.add(Entity.KotlinBlockInjection(source.length, str.length))
                source.append(str)
                source.append("%>")
                return this
            }

            override fun property(def: () -> Pair<String, Type>): TestBuilder {
                val (name, type) = def()
                if (Parser.injectionEndPattern.find(name) != null)
                    throw IllegalArgumentException("Injection closing sequence %> in property name")
                if (name.find { c -> c == ':' } != null)
                    throw IllegalArgumentException("Type separator in property name")
                if (Parser.injectionEndPattern.find(type.compile()) != null)
                    throw IllegalArgumentException("Injection closing sequence %> in property type")
                expectedProperties.add(Property(name.trim(), type))
                source.append("<%@")
                source.append(name)
                source.append(":")
                source.append(type.compile())
                source.append("%>")
                return this
            }

            override fun import(def: () -> String): TestBuilder {
                val str = def()
                if (Parser.injectionEndPattern.find(str) != null)
                    throw IllegalArgumentException("Injection closing sequence %> in injection")
                source.append("<%!")
                expectedImports.add(Import(str))
                source.append(str)
                source.append("%>")
                return this
            }
        }
        test(EntitiesConsumer())
        val actual = Parser.parse(source.toString())
//        assertContentEquals(expectedProperties, actual.properties)
//        assertContentEquals(expectedEntities, actual.entities)
//        assertContentEquals(expectedImports, actual.imports)
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
        property { "name" to Type("Type") }
    }

    @Test
    fun testTwoProperty() = test {
        property { "name1" to Type("Type") }
        property { "name2" to Type("Type") }
    }

    @Test
    fun testTwoPropertiesSeparatedByNewLine() = test {
        property { "name1" to Type("Type") } plain { "\n" }
        property { "name2" to Type("Type") }
    }

    @Test
    fun testTwoPropertiesSeparatedByText() = testCatching<PropertyNotOnTop> {
        property { "name1" to Type("Type") } plain { "aboba" }
        property { "name2" to Type("Type") }
    }

    @Test
    fun testTwoPropertiesSeparatedByEmptyInjection() = testCatching<PropertyNotOnTop> {
        property { "name1" to Type("Type") } inline { "" }
        property { "name2" to Type("Type") }
    }

    @Test
    fun testOneImport() = test {
        import { "kotlin.String" }
    }

    @Test
    fun testTwoImports() = test {
        import { "kotlin.Int" }
        import { "kotlin.UInt" }
    }

    @Test
    fun testTwoImportsSeparatedByNewLine() = test {
        import { "kotlin.Int" } plain { "\n" }
        import { "kotlin.UInt" }
    }

    @Test
    fun testTwoImportsSeparatedByText() = testCatching<ImportNotOnTop> {
        import { "kotlin.Int" } plain { "aboba" }
        import { "kotlin.UInt" }
    }

    @Test
    fun testTwoImportsSeparatedByEmptyInjection() = testCatching<ImportNotOnTop> {
        import { "kotlin.Int" } inline { "" }
        import { "kotlin.UInt" }
    }

    @Test
    fun propertyAfterImport() = test {
        import { "kotlin.Int" }
        property { "num" to Type("Int") }
    }

    @Test
    fun importAfterProperty() = testCatching<ImportNotOnTop> {
        property { "num" to Type("Int") }
        import { "kotlin.Int" }
    }
}