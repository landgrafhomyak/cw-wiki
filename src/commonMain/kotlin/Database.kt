package io.github.landgrafhomyak.chatwars.wiki

interface Database {
    fun getUserBySession(session: String): User
}

inline fun Database.getUserBySession(session: String?) =
    if (session == null) User.Anonymous
    else this.getUserBySession(session)