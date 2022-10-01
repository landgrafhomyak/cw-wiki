package io.github.landgrafhomyak.chatwars.wiki.http

interface Path {
    val topLevelPath: String?
    val subPath: String?
    val queryIterator: Iterator<Map.Entry<String?, String>>
}