@file:JvmName("Parser")

package io.github.landgrafhomyak.chatwars.wiki.preprocessors.html

import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

object Parser {
    internal val injectionStartPattern = Regex("<%([=@!]?)")
    internal val injectionEndPattern = Regex("%>")
    // ^^^ accessed in tests

    @Suppress("ReplaceRangeStartEndInclusiveWithFirstLast")
    @JvmStatic
    fun parse(source: String): Page {
        var pos = 0
        val properties = ArrayList<Property>()
        val entities = ArrayList<Entity>()
        val imports = ArrayList<Import>()

        while (pos < source.length) {
            val startMatch = injectionStartPattern.find(source, pos) ?: break
            if (startMatch.range.start > pos)
                entities.add(Entity.Plain(pos, startMatch.range.start - pos))

            val injectionStart = startMatch.range.endInclusive + 1
            val endMatch = injectionEndPattern.find(source, injectionStart) ?: throw UnclosedInjection()
            val injectionEnd = endMatch.range.start

            when (startMatch.groupValues[1]) {
                ""  -> {
                    entities.add(Entity.KotlinBlockInjection(injectionStart, injectionEnd - injectionStart))
                }
                "=" -> {
                    entities.add(Entity.KotlinInlineInjection(injectionStart, injectionEnd - injectionStart))
                }
                "@" -> {
                    for (e in entities) {
                        if (e !is Entity.Plain || e.extract(source).isNotBlank())
                            throw PropertyNotOnTop()
                    }
                    val (name, type) = source
                        .slice(injectionStart until injectionEnd)
                        .split(":", limit = 2)
                        .map { s -> s.trim() }
                        .also { substrings ->
                            if (substrings.size < 2) throw MissedPropertyType()
                        }
                    @Suppress("RemoveRedundantQualifierName")
                    Parser.checkName(name)
                    properties.add(Property(name, Type.parse(type)))
                }
                "!" -> {
                    if (properties.isNotEmpty()) throw ImportNotOnTop()
                    for (e in entities) {
                        if (e !is Entity.Plain || e.extract(source).isNotBlank())
                            throw ImportNotOnTop()
                    }
                    val t = Type.parse(source.slice(injectionStart until injectionEnd))
                    if (t.generics.isNotEmpty()) throw InvalidImport("Generics in imports not allowed")
                    if (t.nullable) throw InvalidImport("Import can't be nullable")

                    imports.add(Import(t.fullQualifiedName))
                }
            }
            pos = endMatch.range.last + 1
        }
        if (pos < source.length) {
            entities.add(Entity.Plain(pos, source.length - pos))
        }
        return Page(
            imports = imports,
            properties = properties,
            source = source,
            entities = entities
        )
    }

    @JvmStatic
    fun checkName(raw: String) {
        if (raw[0] == '`') {
            if (raw[raw.length - 1] != '`') throw InvalidName("Unclosed grave ` in name")
            if (raw.length == 2) throw InvalidName("Name must be non-empty")
            var i = raw.length - 1
            while (i-- > 1) {
                if (raw[i] == '`')
                    throw InvalidName("Grave ` in the middle of name")
            }
        } else {
            if (raw.isEmpty()) throw InvalidName("Name must be non-empty")
            raw.forEachIndexed { index, c ->
                when (c) {
                    in '0'..'9'                   -> {
                        if (index == 0) throw InvalidName("Name cant start with digit")
                    }
                    in 'a'..'z', in 'A'..'Z', '_' -> {}
                    else                          -> throw InvalidName("Invalid char in name: $c")
                }
            }
        }
    }
}