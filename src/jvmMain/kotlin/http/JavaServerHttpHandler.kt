package io.github.landgrafhomyak.chatwars.wiki.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import io.github.landgrafhomyak.chatwars.wiki.Database
import io.github.landgrafhomyak.chatwars.wiki.PageProcessor
import java.net.URI

class JavaServerHttpHandler(database: Database) : HttpHandler {
    private class URIAsPathWrapper(_native: URI) : Path {
        override val topLevelPath: String?
        override val subPath: String?

        private class QueryEntry(override val key: String?, override val value: String) : Map.Entry<String?, String> {
            var next: QueryEntry? = null
        }

        private val queryStart: QueryEntry?

        init {
            kotlin.run path@{
                @Suppress("LocalVariableName")
                val _path = _native.path
                if (_path.isEmpty() || _path == "/") {
                    this.topLevelPath = null
                    this.subPath = null
                    return@path
                }
                val firstSep = _path.indexOf('/')
                if (firstSep < 0) {
                    this.topLevelPath = _path
                    this.subPath = null
                    return@path
                }
                if (firstSep == _path.length - 1) {
                    this.topLevelPath = _path.slice(0 until firstSep)
                    this.subPath = null
                    return@path
                }

                if (firstSep == 0) {
                    val secondSep = _path.indexOf('/', startIndex = 1)
                    if (secondSep == -1) {
                        this.topLevelPath = _path.slice(1 until _path.length)
                        this.subPath = null
                        return@path
                    }
                    this.topLevelPath = _path.slice(1 until secondSep)
                    if (secondSep == _path.length - 1) {
                        this.subPath = null
                    } else {
                        this.subPath = _path.slice((secondSep + 1) until _path.length)
                    }
                } else {
                    this.topLevelPath = _path.slice(0 until firstSep)
                    this.subPath = _path.slice((firstSep + 1) until _path.length)
                }
            }

            kotlin.run query@{
                if (_native.query == null) {
                    this.queryStart = null
                    return@query
                }

                var start: QueryEntry? = null
                var end: QueryEntry? = null
                for (_entry in _native.query.split('&')) {
                    val sep = _entry.indexOf('=')
                    val entry =
                        if (sep < 0) QueryEntry(null, _entry)
                        else QueryEntry(_entry.slice(0 until sep), _entry.slice((sep + 1) until _entry.length))
                    if (end != null)
                        end.next = entry
                    else
                        start = entry
                    end = entry
                }
                this.queryStart = start
            }
        }

        private class QueryIterator(private var node: QueryEntry?) : Iterator<Map.Entry<String?, String>> {
            override fun hasNext(): Boolean = this.node != null

            override fun next(): Map.Entry<String?, String> {
                val ret = this.node ?: throw NoSuchElementException()
                this.node = ret.next
                return ret
            }
        }

        override val queryIterator: Iterator<Map.Entry<String?, String>>
            get() = QueryIterator(this.queryStart)
    }


    private val proc = PageProcessor(database)
    override fun handle(exchange: HttpExchange?) {
        exchange ?: return
        val wrapper = JavaHttpExchangeLazyWrapper(exchange)
        this.proc.process(URIAsPathWrapper(exchange.requestURI), wrapper)
        for ((name, value) in wrapper.outputHeaders)
            exchange.responseHeaders.add(name, value)
        exchange.sendResponseHeaders(wrapper.responseCode!!, wrapper.body!!.size.toLong())
        exchange.responseBody.write(wrapper.body!!)
        exchange.responseBody.close()
    }
}