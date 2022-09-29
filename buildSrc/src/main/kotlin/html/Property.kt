package io.github.landgrafhomyak.chatwars.wiki.preprocessors.html

@Suppress("EqualsOrHashCode")
class Property(val name: String, val type: Type) {
    inline operator fun component1() = this.name
    inline operator fun component2() = this.type
    override fun toString(): String = "<property name=`${this.name}` type=`${this.type}`>"

    override fun equals(other: Any?): Boolean {
        other ?: return false
        if (other !is Property) return false
        return this.name == other.name && this.type == other.type
    }

    inline val gravedName: String
        get() {
            @Suppress("LiftReturnOrAssignment")
            if (this.name.startsWith("`"))
                return this.name
            else
                return "`${this.name}`"
        }
}