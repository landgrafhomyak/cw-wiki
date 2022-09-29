package io.github.landgrafhomyak.chatwars.wiki.preprocessors.res

import kotlin.jvm.JvmStatic

object Generator {
    @JvmStatic
    fun generate(
        pkg: String, name: String,
        textFiles: Map<String, String>,
        binaryFiles: Map<String, ByteArray>
    ): String {
        val builder = StringBuilder("package ${pkg};\n")
        builder.append("import kotlin.jvm.JvmStatic\n\n")
        builder.append("public object $name {")
        for ((prop, res) in binaryFiles) {
            builder.append("    @JvmStatic\n")
            builder.append("    public val ${prop}: ByteArray = byteArrayOf(")
            builder.append(res.joinToString(", ") { b -> b.toString() })
            builder.append(")\n\n")
        }
        for ((prop, res) in textFiles) {
            builder.append("    @JvmStatic\n")
            builder.append("    public val ${prop}: String = \"")
            builder.append(res.asIterable().joinToString(separator = "") { c -> "\\u${c.toShort().toString(16).padStart(4, '0')}" })
            builder.append("\"\n\n")
        }
        builder.append("}")
        return builder.toString()
    }
}