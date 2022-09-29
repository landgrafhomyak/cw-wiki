package io.github.landgrafhomyak.chatwars.wiki

interface ResourcesProvider {
    fun get(path: String): ByteArray
}