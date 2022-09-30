package io.github.landgrafhomyak.chatwars.wiki

import kotlin.jvm.JvmStatic

object PageProcessor {
    @JvmStatic
    fun process(path: String, resp: HttpExchange) {
        when (path) {
            "/"           -> {
                resp.responseCode(200)
                resp.setContentType(HttpContentType.HTML)
                resp.body(
                    PageGenerator.generate(
                        title = "Main page",
                        user = User.Authorized(UserId(0u), "abc"),
                        page = Page.Article.Article()
                    )
                )
            }
            "/common.css" -> {
                resp.responseCode(200)
                resp.setContentType(HttpContentType.CSS)
                resp.body(Resources.cssCommon)
            }
        }
    }
}