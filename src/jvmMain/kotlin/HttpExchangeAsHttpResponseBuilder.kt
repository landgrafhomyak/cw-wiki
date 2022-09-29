package io.github.landgrafhomyak.chatwars.wiki

import com.sun.net.httpserver.HttpExchange

@JvmInline
value class HttpExchangeAsHttpResponseBuilder(private val _native: HttpExchange) : HttpResponseBuilder {
    override fun answer(code: Int, body: ByteArray) {
        this._native.sendResponseHeaders(code, body.size.toLong())
        this._native.responseBody.write(body)
    }

    override fun answer(code: Int, body: String) = this.answer(code, body.toByteArray(Charsets.UTF_8))
}