package io.github.landgrafhomyak.chatwars.wiki

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler

object HttpHandler : HttpHandler {
    private val proc = PageProcessor(EmbedResourceProvider)
    override fun handle(exchange: HttpExchange?) {
        exchange ?: return

        this.proc.process(exchange.requestURI.path, HttpExchangeAsHttpResponseBuilder(exchange))

        exchange.responseBody.close()
    }
}