package io.github.landgrafhomyak.chatwars.wiki

import io.github.landgrafhomyak.chatwars.wiki.templates.Root
import kotlin.jvm.JvmStatic

object PageProcessor {
    @JvmStatic
    fun process(path: String, resp: HttpResponseBuilder) {
        when (path) {
            "/"           -> resp.answer(200, Root.format("Main page"))
            "/common.css" -> resp.answer(200, Resources.cssCommon)
        }
    }
}