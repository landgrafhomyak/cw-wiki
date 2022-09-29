package io.github.landgrafhomyak.chatwars.wiki

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler

object HttpHandler : HttpHandler {
    override fun handle(exchange: HttpExchange?) {
        exchange ?: return

        PageProcessor.process(exchange.requestURI.path, HttpExchangeAsHttpResponseBuilder(exchange))

        exchange.responseBody.close()
    }
}