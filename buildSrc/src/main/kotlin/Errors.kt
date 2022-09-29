package io.github.landgrafhomyak.chatwars.wiki.html_compiler

sealed class HtmlCompilationError(final override val message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class MissedPropertyType : HtmlCompilationError("Property must be specified after semicolon (:)")
class PropertyNotOnTop : HtmlCompilationError("Property must be at top of file, but under imports")
class ImportNotOnTop : HtmlCompilationError("Import must be at top of file")
class UnclosedInjection : HtmlCompilationError("Injection must be closed with %>")

class InvalidName(message: String, cause: Throwable? = null) : HtmlCompilationError(message, cause)
class InvalidType(message: String, cause: Throwable? = null) : HtmlCompilationError(message, cause)
class InvalidImport(message: String, cause: Throwable? = null) : HtmlCompilationError(message, cause)