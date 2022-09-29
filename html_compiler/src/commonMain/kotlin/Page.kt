package io.github.landgrafhomyak.chatwars.wiki.html_compiler

class Page(
    val imports: List<Import>,
    val properties: List<Property>,
    val source: String,
    val entities: List<Entity>
) {
    inline fun Entity.extract(): String = this@extract.extract(this@Page.source)

    fun htmlOnlyString(): String {
        val builder = StringBuilder()
        for (e in this.entities) {
            if (e is Entity.Plain)
                builder.append(e.extract())
        }
        return builder.toString()
    }
}