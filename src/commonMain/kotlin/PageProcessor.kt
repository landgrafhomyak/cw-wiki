package io.github.landgrafhomyak.chatwars.wiki

import io.github.landgrafhomyak.chatwars.wiki.templates.Root

class PageProcessor(private val resources: ResourcesProvider) {
    fun process(path: String, resp: HttpResponseBuilder) {
        when (path) {
            "/"           -> resp.answer(200, Root.format("Main page"))
            "/common.css" -> resp.answer(200, this.resources.get("/common.css"))
        }
    }
}