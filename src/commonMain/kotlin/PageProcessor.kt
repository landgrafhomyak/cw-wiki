package io.github.landgrafhomyak.chatwars.wiki

import kotlin.jvm.JvmStatic

object PageProcessor {
    @JvmStatic
    fun process(path: String, resp: HttpResponseBuilder) {
        when (path) {
            "/"           -> resp.answer(
                200, PageGenerator.generate(
                    title = "Main page",
                    user = User.Authorized(UserId(0u), "abc"),
                    page = Page.Article.Article()
                )
            )
            "/common.css" -> resp.answer(200, Resources.cssCommon)
        }
    }
}