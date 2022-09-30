package io.github.landgrafhomyak.chatwars.wiki

interface HttpExchange {
    fun responseCode(code: Int)
    fun getInputHeader(key: String): String?
    fun addOutputHeader(key: String, value: String)
    fun body(body: ByteArray)
    fun body(body: String) = this.body(body.encodeToByteArray())
}


enum class HttpContentType(val value: String) {
    HTML("text/html"),
    CSS("text/css"),
}

inline fun HttpExchange.setContentType(type: HttpContentType, charset: String = "utf-8") =
    this.addOutputHeader("Content-Type", type.value + "; charset=${charset}")
