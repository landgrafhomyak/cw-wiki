package io.github.landgrafhomyak.chatwars.wiki

import kotlin.jvm.JvmStatic

object PageGenerator {
    @Suppress("NOTHING_TO_INLINE")
    @JvmStatic
    private inline fun <reified T : Page> StringBuilder.addHeaderTab(page: Page, url: String, title: String, joinLeft: Boolean) {
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


    @JvmStatic
    fun generate(title: String, user: User, page: Page) = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link rel="stylesheet" href="/common.css">
</head>
<body>
<div class="navbar">
    <a href='/'>
        <img class="logo" src="/logo.svg">
    </a>
    <a href="/">На главную</a>
    <a href="https://telegram.me/chtwrsbot?start=75ad693c5cea4021b2a275847887f8f2">Чат</a>
    <a href="/random">Случайная страница</a>
    <hr>
    <a href="/last_edits">Последние изменения</a>
    <a href="/unchecked_edits">Непроверенные изменения</a>
    <a href="/redirects">Перенаправления</a>
</div>
<div class="header">
    <div class="account">
        ${
            when (user) {
                is User.SessionExpired -> """
                        <b style="color: darkred">Ваша сессия истекла!</b>
                        <a href="#">Ок</a>
                        <a href="/login">Войти ещё раз</a>
                        <div class="sep"></div>
                        <a href='/user/${user.name}'><s>${user.name}</s></a>
                        <div class="sep"></div>
                        <a href='/user/${user.name}?contribution'>Вклад</a>
                    """
                is User.Authorized     -> """
                        <a href='/user/${user.name}'>${user.name}</a>
                        <div class="sep"></div>
                        <a href='/user/${user.name}?contribution'>Вклад</a>
                        <a href='/logout'>Выйти</a>
                    """
                else                   -> "<a href='/login'>Войти</a>"

            }
        }
    </div>
    <div class="tabs">
        ${
            when (page) {
                is Page.Article -> {
                    val s = StringBuilder()
                    s.addHeaderTab<Page.Article.Article>(page, ".", "Статья", false)
                    s.addHeaderTab<Page.Article.Fix>(page, "?fix", "Предложить исправление", true)
                    if (user is User.Authorized) {
                        if (page is Page.Article.Source) 
                            s.addHeaderTab<Page.Article.Source>(page, "?source", "Исходный код", true)
                        else 
                            s.addHeaderTab<Page.Article.Edit>(page, "?edit", "Редактировать", true)
                    }
                    else
                    {
                        s.addHeaderTab<Page.Article.Source>(page, "?source", "Исходный код", true)
                    }
                    s.addHeaderTab<Page.Article.History>(page, "?history", "История", true)
                    s.toString()
                }
                is Page.User    -> {
                    val s = StringBuilder()
                    s.addHeaderTab<Page.User.Profile>(page, ".", "Пользователь", false)
                    s.addHeaderTab<Page.User.Contribution>(page, "?contribution", "Вклад", true)
                    s.toString()
                }
                is Page.Special -> ""
                else            -> ""
            }
        }
        <div class="space-tab">
        </div>
    </div>
</div>
<div class="body"></div>
<div class="footer"></div>
</body>
</html>
"""
}