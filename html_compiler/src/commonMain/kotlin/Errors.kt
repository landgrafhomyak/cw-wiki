package io.github.landgrafhomyak.chatwars.wiki.html_compiler

sealed class HtmlCompilationError(override val message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class MissedPropertyType : HtmlCompilationError("Property must be specified after semicolon (:)")
class PropertyNotOnTop : HtmlCompilationError("Property must be at top of file")
class UnclosedInjection : HtmlCompilationError("Injection must be closed with %>")