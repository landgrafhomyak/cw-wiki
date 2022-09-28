package io.github.landgrafhomyak.chatwars.wiki.html_compiler

@Suppress("EqualsOrHashCode")
sealed class Entity
constructor(val start: UInt, val length: UInt) {
    inline fun extract(source: CharSequence) =
        source.slice(this.start.toInt() until ((this.start + this.length).toInt()))

    inline fun extract(source: String) =
        source.slice(this.start.toInt() until ((this.start + this.length).toInt()))

    override fun equals(other: Any?): Boolean {
        other ?: return false
        if (this::class != other::class)
            return false
        other as Entity
        return this.start == other.start && this.length == other.length
    }

    class Plain(start: UInt, length: UInt) : Entity(start, length) {
        override fun toString(): String = "<entity 'plain' start=${this.start} length=${this.length}>"
    }
    class KotlinInlineInjection(start: UInt, length: UInt) : Entity(start, length) {
        override fun toString(): String = "<entity 'inline' start=${this.start} length=${this.length}>"
    }
    class KotlinBlockInjection(start: UInt, length: UInt) : Entity(start, length) {
        override fun toString(): String = "<entity 'block' start=${this.start} length=${this.length}>"
    }
}