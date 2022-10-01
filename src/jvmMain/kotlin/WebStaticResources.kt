package io.github.landgrafhomyak.chatwars.wiki

actual object WebStaticResources {
    actual val cssCommon: ByteArray = WebStaticResources::class.java.getResourceAsStream("/common.css")!!.readAllBytes()
    actual val logo: ByteArray = WebStaticResources::class.java.getResourceAsStream("/logo/cw3-default.svg")!!.readAllBytes()
}