package io.github.landgrafhomyak.chatwars.wiki.preprocessors.html

@Suppress("EqualsOrHashCode")
sealed class Entity
constructor(val start: Int, val length: Int) {
    @Suppress("NOTHING_TO_INLINE")
    inline fun extract(source: CharSequence) =
        source.slice(this.start until (this.start + this.length))

    @Suppress("NOTHING_TO_INLINE")
    inline fun extract(source: String) =
        source.slice(this.start until (this.start + this.length))

    override fun equals(other: Any?): Boolean {
        other ?: return false
        if (this::class != other::class)
            return false
        other as Entity
        return this.start == other.start && this.length == other.length
    }

    class Plain(start: Int, length: Int) : Entity(start, length) {
        override fun toString(): String = "<entity 'plain' start=${this.start} length=${this.length}>"
    }

    class KotlinInlineInjection(start: Int, length: Int) : Entity(start, length) {
        override fun toString(): String = "<entity 'inline' start=${this.start} length=${this.length}>"
    }

    class KotlinBlockInjection(start: Int, length: Int) : Entity(start, length) {
        override fun toString(): String = "<entity 'block' start=${this.start} length=${this.length}>"
    }
}