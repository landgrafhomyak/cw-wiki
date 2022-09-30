package io.github.landgrafhomyak.chatwars.wiki

@Suppress("RemoveRedundantQualifierName")
sealed class Page {
    sealed class Article : Page() {
        class Article : Page.Article()
        class Fix : Page.Article()
        class Source : Page.Article()
        class Edit : Page.Article()
        class History : Page.Article()
    }

    sealed class User : Page() {
        class Profile : User()
        class Contribution : User()
    }

    sealed class Special : Page()
    sealed class ServerError : Page()
}