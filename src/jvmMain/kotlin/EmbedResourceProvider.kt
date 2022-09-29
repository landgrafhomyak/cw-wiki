package io.github.landgrafhomyak.chatwars.wiki

object EmbedResourceProvider : ResourcesProvider {
    override fun get(path: String): ByteArray {
        return EmbedResourceProvider::class.java.getResourceAsStream(path)!!.readAllBytes()
    }
}