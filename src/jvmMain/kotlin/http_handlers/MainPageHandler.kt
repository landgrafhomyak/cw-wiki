package io.github.landgrafhomyak.chatwars.wiki.http_handlers

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler

object MainPageHandler : HttpHandler {
    override fun handle(exchange: HttpExchange?) {
        exchange ?: return
        if (exchange.requestURI.path != "/") return
        exchange.sendResponseHeaders(200, 4)
        exchange.responseBody
            .apply { write("qwer".toByteArray()) }
            .close()
    }
}