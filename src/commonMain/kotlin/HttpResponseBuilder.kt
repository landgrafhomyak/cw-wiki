package io.github.landgrafhomyak.chatwars.wiki

interface HttpResponseBuilder {
    fun answer(code: Int, body: ByteArray)
    fun answer(code: Int, body: String) = this.answer(code, body.encodeToByteArray())
}