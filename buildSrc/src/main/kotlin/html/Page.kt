package io.github.landgrafhomyak.chatwars.wiki.preprocessors.html

class Page(
    val imports: List<Import>,
    val properties: List<Property>,
    val source: String,
    val entities: List<Entity>
) {
    @Suppress("NOTHING_TO_INLINE")
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