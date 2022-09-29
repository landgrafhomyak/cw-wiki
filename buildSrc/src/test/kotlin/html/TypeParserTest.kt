package io.github.landgrafhomyak.chatwars.wiki.preprocessors.html

import io.github.landgrafhomyak.chatwars.wiki.preprocessors.html.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class TypeParserTest {
    private fun test(fullQualifiedName: String, nullable: Boolean = false, vararg generics: Type) {
        if (fullQualifiedName.endsWith("?"))
            throw IllegalArgumentException("Use 'nullable' argument instead of inserting ? to 'name' parameter")

        val req = Type(fullQualifiedName, nullable, *generics)
        assertEquals(req, Type.parse(req.compile()))
    }

    private inline fun test(fullQualifiedName: String, vararg generics: Type) =
        this.test(fullQualifiedName, false, *generics)

    @Test
    fun justName() = test("String")

    @Test
    fun nullableJustName() = test("String", true)

    @Test
    fun fullQualifiedName() = test("kotlin.reflect.KClass")

    @Test
    fun nullableFullQualifiedName() = test("kotlin.reflect.KClass", true)

    @Test
    fun justNameWithSimplestGeneric() = test(
        "Array",
        Type("String")
    )

    @Test
    fun justNameWithTwoSimplestGenerics() = test(
        "Pair",
        Type("Int"), Type("String")
    )

    @Test
    fun twoQualifiedNameGenerics() = test(
        "kotlin.Pair",
        Type("kotlin.Int"), Type("kotlin.reflect.KClass")
    )

    @Test
    fun genericInGeneric() = test(
        "kotlin.reflect.KCLass",
        Type("kotlin.Array", Type("String"))
    )

    @Test
    fun moreGenerics() = test(
        "kotlin.Pair",
        Type(
            "kotlin.Pair",
            Type("kotlin.Pair", Type("kotlin.Byte"), Type("UByte")),
            Type("kotlin.Pair", Type("Short"), Type("kotlin.UShort"))
        ),
        Type(
            "kotlin.Pair",
            Type("kotlin.Pair", Type("Int"), Type("kotlin.UInt")),
            Type("kotlin.Pair", Type("kotlin.Long"), Type("ULong"))
        )
    )

    @Test
    fun someOfThemNullable() = test(
        "kotlin.Pair",
        Type(
            "kotlin.Pair", true,
            Type("kotlin.Pair", Type("kotlin.Byte"), Type("UByte", true)),
            Type("kotlin.Pair", Type("Short"), Type("kotlin.UShort", true))
        ),
        Type(
            "kotlin.Pair", true,
            Type("kotlin.Pair", Type("Int", true), Type("kotlin.UInt")),
            Type("kotlin.Pair", true, Type("kotlin.Long"), Type("ULong"))
        ),
    )

    @Test
    fun manySpacesAndGenerics() = assertEquals(
        Type("kotlin.Pair", true, Type("UInt", true), Type("String", true)),
        Type.parse("   kotlin    .   Pair         <         UInt    ?     ,      String     ? >     ?")
    )

    @Test
    fun unclosedGeneric() {
        assertFails {
            Type.parse("a<")
        }
    }

    @Test
    fun redundantComma() {
        assertFails {
            Type.parse("a<b, >")
        }
    }

    @Test
    fun dotInName() {
        assertFails {
            Type.parse("`a.b`")
        }
    }

    @Test
    fun namesWithGraves() = test(
        "abadvav.`warwe`", true,
        Type("rem.`fdopksdf`.rewreqwer", true, Type("`ffef`", true))
    )

    @Test
    fun genericInGravedName() {
        assertFails {
            Type.parse("`a<>`<b>")
        }
    }

    @Test
    fun sequentGenerics() {
        assertFails {
            Type.parse("a<b><c>")
        }
    }

    @Test
    fun emptyGenerics() {
        assertFails {
            Type.parse("a<>")
        }
    }
}