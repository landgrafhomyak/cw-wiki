package io.github.landgrafhomyak.chatwars.wiki.html_compiler

import kotlin.jvm.JvmStatic

class Type(val fullQualifiedName: String, val nullable: Boolean, val generics: List<Type>) {
    constructor(fullQualifiedName: String, nullable: Boolean = false, vararg generics: Type) :
            this(fullQualifiedName, nullable, generics.asList())

    constructor(fullQualifiedName: String, vararg generics: Type) :
            this(fullQualifiedName, false, generics.asList())

    companion object {
        @JvmStatic
        fun parse(raw: String): Type {
            val nullable: Boolean

            @Suppress("NAME_SHADOWING")
            val raw = raw.trim().let { raw ->
                if (raw.endsWith('?')) {
                    nullable = true
                    return@let raw.slice(0 until (raw.length - 1)).trimEnd()
                } else {
                    nullable = false
                    return@let raw
                }
            }

            val genericsStack = ArrayList<Int>()
            val generics = ArrayList<Type>()

            @Suppress("LocalVariableName")
            var _fullQualifiedName: String? = null
            raw.forEachIndexed { pos, c ->
                when (c) {
                    ',' -> {
                        if (genericsStack.size == 1) {
                            @Suppress("RemoveRedundantQualifierName")
                            generics.add(Type.parse(raw.slice(genericsStack.last() until pos)))
                            genericsStack[0] = pos + 1
                        }
                    }
                    '<' -> {
                        if (genericsStack.isEmpty()) {
                            if (_fullQualifiedName != null) throw InvalidType("Sequent generics not allowed")
                            _fullQualifiedName = raw.slice(0 until pos)
                        }
                        genericsStack.add(pos + 1)
                    }
                    '>' -> {
                        val start = genericsStack.removeLastOrNull() ?: throw InvalidType("Too many > in generic definition")
                        if (genericsStack.isEmpty()) {
                            val lastGeneric = raw.slice(start until pos)
                            if (lastGeneric.isBlank()) throw InvalidType("Redundant comma ,")
                            @Suppress("RemoveRedundantQualifierName")
                            generics.add(Type.parse(lastGeneric))
                        }
                    }
                }
            }
            if (genericsStack.isNotEmpty()) throw InvalidType("Unclosed generic")

            if (_fullQualifiedName != null && generics.isEmpty()) throw InvalidType("Empty generic")

            val fullQualifiedName = _fullQualifiedName ?: raw

            val name: String
            val pkg = fullQualifiedName.split(".")
                .map { s -> s.trim() }
                .also { l -> name = l.last() }
                .dropLast(1)
                .onEach { s -> Parser.checkName(s) }
                .joinToString(separator = ".")

            Parser.checkName(name)
            return Type(if (pkg.isEmpty()) name else "$pkg.$name", nullable, generics)
        }
    }


    fun compile(): String {
        val builder = StringBuilder(this.fullQualifiedName)
        if (this.generics.isNotEmpty()) {
            builder.append(
                this.generics.joinToString(
                    separator = ", ",
                    prefix = "<",
                    postfix = ">",
                    transform = Type::compile
                )
            )
        }
        if (this.nullable)
            builder.append("?")
        return builder.toString()
    }

    override fun toString(): String = "<property type: ${this.compile()}>"

    override fun equals(other: Any?): Boolean {
        other ?: return false
        if (other !is Type) return false

        return this.fullQualifiedName == other.fullQualifiedName && this.nullable == other.nullable && this.generics == other.generics
    }

    override fun hashCode(): Int {
        var result = fullQualifiedName.hashCode()
        result = 31 * result + nullable.hashCode()
        result = 31 * result + generics.hashCode()
        return result
    }
}