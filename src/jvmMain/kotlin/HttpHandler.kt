package io.github.landgrafhomyak.chatwars.wiki

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler

object HttpHandler : HttpHandler {
    override fun handle(exchange: HttpExchange?) {
        exchange ?: return
        val wrapper = JavaHttpExchangeLazyWrapper(exchange)
        PageProcessor.process(exchange.requestURI.path, wrapper)
        exchange.sendResponseHeaders(wrapper.responseCode!!, wrapper.body!!.size.toLong())
        for ((name, value) in wrapper.outputHeaders)
            exchange.responseHeaders.add(name, value)
        exchange.responseBody.write(wrapper.body!!)
        exchange.responseBody.close()
    }
}