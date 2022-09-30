package io.github.landgrafhomyak.chatwars.wiki

sealed class Page {
    sealed class Article : Page() {
        class Article : Page.Article()
        class Fix : Page.Article()
        class Source : Page.Article()
        class Edit : Page.Article()
        class History : Page.Article()
    }

    sealed class User : Page()
    sealed class Special : Page()
}