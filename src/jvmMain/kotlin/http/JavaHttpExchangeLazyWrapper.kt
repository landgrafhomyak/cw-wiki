package io.github.landgrafhomyak.chatwars.wiki.http

class JavaHttpExchangeLazyWrapper(private val _native: com.sun.net.httpserver.HttpExchange) : HttpExchange {
    var responseCode: Int? = null
        private set

    override fun responseCode(code: Int) {
        if (this.responseCode != null) throw IllegalStateException("Redeclaration of exit code")
        this.responseCode = code
    }

    override fun getInputHeader(key: String): String? = this._native.requestHeaders.getFirst(key)

    private class Header(
        override val key: String,
        override val value: String
    ) : Map.Entry<String, String> {
        var next: Header? = null
    }

    private var _outputHeadersStart: Header? = null
    private var _outputHeadersEnd: Header? = null

    override fun addOutputHeader(key: String, value: String) {
        val node = Header(key, value)
        if (this._outputHeadersEnd != null)
            this._outputHeadersEnd!!.next = node
        else
            this._outputHeadersStart = node
        this._outputHeadersEnd = node
    }


    private class HeadersIterator(private var node: Header?) : Iterator<Map.Entry<String, String>> {
        override fun hasNext(): Boolean = this.node != null

        override fun next(): Map.Entry<String, String> {
            val ret = this.node ?: throw NoSuchElementException()
            this.node = ret.next
            return ret
        }
    }

    val outputHeaders: Iterator<Map.Entry<String, String>>
        get() = HeadersIterator(this._outputHeadersStart)

    var body: ByteArray? = null
        private set

    override fun body(body: ByteArray) {
        if (this.body != null) throw IllegalStateException("Redeclaration of body")
        this.body = body
    }

    override fun body(body: String) {
        if (this.body != null) throw IllegalStateException("Redeclaration of body")
        this.body = body.toByteArray(Charsets.UTF_8)
    }
}