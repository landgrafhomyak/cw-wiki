package io.github.landgrafhomyak.chatwars.wiki

import io.github.landgrafhomyak.chatwars.wiki.templates.Root
import kotlin.jvm.JvmStatic

object PageProcessor {
    @JvmStatic
    fun process(path: String, resp: HttpResponseBuilder) {
        when (path) {
            "/"           -> resp.answer(200, Root.format {
                title = "Main page"
                user = User.Authorized(UserId(0u), "abc")
            })
            "/common.css" -> resp.answer(200, Resources.cssCommon)
        }
    }
}