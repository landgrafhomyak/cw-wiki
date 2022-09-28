@file:JvmName("Parser")

package io.github.landgrafhomyak.chatwars.wiki.html_compiler

import kotlin.jvm.JvmName

internal val injectionStartPattern = Regex("<%([=@]?)")
internal val injectionEndPattern = Regex("%>")
// ^^^ accessed in tests

@Suppress("ReplaceRangeStartEndInclusiveWithFirstLast")
fun parse(source: CharSequence): Pair<List<Property>, List<Entity>> {
    var pos = 0
    val properties = ArrayList<Property>()
    val entities = ArrayList<Entity>()
    while (pos < source.length) {
        val startMatch = injectionStartPattern.find(source, pos) ?: break
        if (startMatch.range.start > pos)
            entities.add(Entity.Plain(pos.toUInt(), (startMatch.range.start - pos).toUInt()))

        val injectionStart = startMatch.range.endInclusive + 1
        val endMatch = injectionEndPattern.find(source, injectionStart) ?: throw UnclosedInjection()
        val injectionEnd = endMatch.range.start

        when (startMatch.groupValues[1]) {
            ""  -> {
                entities.add(Entity.KotlinBlockInjection(injectionStart.toUInt(), (injectionEnd - injectionStart).toUInt()))
            }
            "=" -> {
                entities.add(Entity.KotlinInlineInjection(injectionStart.toUInt(), (injectionEnd - injectionStart).toUInt()))
            }
            "@" -> {
                for (e in entities) {
                    if (e !is Entity.Plain || e.extract(source).isNotBlank())
                        throw PropertyNotOnTop()
                }
                val (name, type) = source
                    .slice(injectionStart until injectionEnd)
                    .split(":", limit = 2)
                    .also { substrings ->
                        if (substrings.size < 2) throw MissedPropertyType()
                    }
                properties.add(Property(name, type))
            }
        }
        pos = endMatch.range.last + 1
    }
    if (pos < source.length) {
        entities.add(Entity.Plain(pos.toUInt(), ((source.length - pos).toUInt())))
    }
    return properties to entities
}