package io.github.landgrafhomyak.chatwars.wiki.templates

import io.github.landgrafhomyak.chatwars.wiki.Page

@Suppress("NOTHING_TO_INLINE")
internal inline fun <reified T : Page> StringBuilder.addHeaderTab(page: Page, url: String, title: String, joinLeft: Boolean) {
    this.append(
        """<div class='tab${
            if (joinLeft) " join-left"
            else ""
        }${
            if (page is T) " selected'>$title"
            else "'><a href='${url}'>${title}</a>"
        }</div>"""
    )
}