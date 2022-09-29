package io.github.landgrafhomyak.chatwars.wiki.html_compiler

import kotlin.jvm.JvmStatic


object Generator {
    @JvmStatic
    fun generate(pkg: String, name: String, page: Page): String {
        if (page.properties.map { prop -> prop.gravedName }.toSet().size != page.properties.size)
            throw IllegalArgumentException("Property name duplication")

        val builder = StringBuilder("package $pkg\n\n")
        for (import in page.imports) {
            builder.append("import ${import.qualifiedName}\n")
        }

        builder.append("\n")
        builder.append("public class $name public constructor() {\n")
        for (property in page.properties) {
            builder.append("    public lateinit var ${property.name}: ${property.type.compile()}\n")
        }

        builder.append("    public companion object {\n")
        builder.append("        @kotlin.jvm.JvmStatic\n")
        builder.append("        public fun format(params: $name): String = this(\n")
        for (property in page.properties) {
            builder.append("            ${property.name} = params.${property.name},\n")
        }
        builder.append("        )\n\n")
        builder.append("        @kotlin.jvm.JvmStatic")
        builder.append("        public fun format(params: ${name}.() -> Unit): String {\n")
        builder.append("            val _params = ${name}()\n")
        builder.append("            params(_params)\n")
        builder.append("            return this(_params)\n")
        builder.append("        )\n\n")
        builder.append("        @kotlin.jvm.JvmStatic")
        builder.append("        public fun format(\n")
        for (property in page.properties) {
            builder.append("            ${property.name}: ${property.type.compile()},\n")
        }
        builder.append("        ) = \n\"\"\"")
        for (e in page.entities) {
            when (e) {
                is Entity.Plain                 -> builder.append(e.extract(page.source).replace("$", "\\$"))
                is Entity.KotlinBlockInjection  -> {
                    builder.append("\${ run {\n")
                    builder.append(e.extract(page.source))
                    builder.append("\nreturn@run \"\" } }")
                }
                is Entity.KotlinInlineInjection -> {
                    builder.append("\${")
                    builder.append(e.extract(page.source))
                    builder.append("}")
                }
            }
        }
        builder.append("\"\"\"\n")
        builder.append("    }\n")
        builder.append("}")
        return builder.toString()
    }
}