package io.github.landgrafhomyak.chatwars.wiki


class PageProcessor(private val database: Database) {
    fun process(path: String, exchange: HttpExchange) {
        val cookies = Cookies(exchange.getInputHeader("Cookie"))
        val user = this.database.getUserBySession(cookies.sessionId)
        when (path) {
            "/"           -> {
                exchange.responseCode(200)
                exchange.setContentType(HttpContentType.HTML)
                exchange.body(
                    PageGenerator.generate(
                        title = "Main page",
                        user = user,
                        page = Page.Article.Article()
                    )
                )
            }
            "/common.css" -> {
                exchange.responseCode(200)
                exchange.setContentType(HttpContentType.CSS)
                exchange.body(WebStaticResources.cssCommon)
            }
            "/logo.svg" -> {
                exchange.responseCode(200)
                exchange.setContentType(HttpContentType.SVG)
                exchange.body(WebStaticResources.logo)
            }
        }
    }
}