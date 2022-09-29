@file:JvmName("ResourcesKt")

package io.github.landgrafhomyak.chatwars.wiki

import kotlin.jvm.JvmName

@Suppress("RemoveRedundantQualifierName")
val SQLiteRequests.createTables: List<String>
    get() = SQLiteRequests._createTables
        .split(";")
        .mapNotNull { raw -> if (raw.isBlank()) null else raw.trim() }

