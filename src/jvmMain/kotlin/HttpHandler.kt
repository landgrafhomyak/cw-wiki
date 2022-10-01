package io.github.landgrafhomyak.chatwars.wiki

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler

class HttpHandler(database: Database) : HttpHandler {
    private val proc = PageProcessor(database)
    override fun handle(exchange: HttpExchange?) {
        exchange ?: return
        val wrapper = JavaHttpExchangeLazyWrapper(exchange)
        this.proc.process(exchange.requestURI.path, wrapper)
        for ((name, value) in wrapper.outputHeaders)
            exchange.responseHeaders.add(name, value)
        exchange.sendResponseHeaders(wrapper.responseCode!!, wrapper.body!!.size.toLong())
        exchange.responseBody.write(wrapper.body!!)
        exchange.responseBody.close()
    }
}