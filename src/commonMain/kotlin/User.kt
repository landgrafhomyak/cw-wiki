package io.github.landgrafhomyak.chatwars.wiki

sealed class User {
    class Authorized(val id: UserId, val name: String, val isAutoPatrol: Boolean, val isAdmin: Boolean) : User()
    class SessionExpired(val id: UserId, val name: String) : User()
    object Anonymous : User()
}