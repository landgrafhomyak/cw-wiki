package io.github.landgrafhomyak.chatwars.wiki

import io.github.landgrafhomyak.chatwars.wiki.http.Cookies
import io.github.landgrafhomyak.chatwars.wiki.http.HttpContentType
import io.github.landgrafhomyak.chatwars.wiki.http.HttpExchange
import io.github.landgrafhomyak.chatwars.wiki.http.Path
import io.github.landgrafhomyak.chatwars.wiki.http.setContentType


class PageProcessor(private val database: Database) {
    fun process(path: Path, exchange: HttpExchange) {
        try {
            val cookies = Cookies(exchange.getInputHeader("Cookie"))
            val user = this.database.getUserBySession(cookies.sessionId)
            when (path.topLevelPath) {
                null         -> {
                    exchange.responseCode(200)
                    exchange.setContentType(HttpContentType.HTML)
                    exchange.body(
                        PageGenerator.generate(
                            title = "Main page",
                            user = user,
                            page = when (path.queryIterator.asSequence().firstOrNull()?.value) {
                                "fix"     -> Page.Article.Fix()
                                "edit"    ->
                                    if (user is User.Authorized) Page.Article.Edit()
                                    else Page.Article.Source()
                                "source"  -> Page.Article.Source()
                                "history" -> Page.Article.History()
                                else      -> Page.Article.Article()
                            }
                        )
                    )
                }
                "common.css" -> {
                    exchange.responseCode(200)
                    exchange.setContentType(HttpContentType.CSS)
                    exchange.body(WebStaticResources.cssCommon)
                }
                "logo.svg"   -> {
                    exchange.responseCode(200)
                    exchange.setContentType(HttpContentType.SVG)
                    exchange.body(WebStaticResources.logo)
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}