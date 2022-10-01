package io.github.landgrafhomyak.chatwars.wiki

import kotlin.jvm.JvmInline

@JvmInline
value class UserId(val realValue: ULong)

inline fun Long.toUserId() = UserId(this.toULong())
inline fun ULong.toUserId() = UserId(this)