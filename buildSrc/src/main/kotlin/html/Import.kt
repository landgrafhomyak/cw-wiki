package io.github.landgrafhomyak.chatwars.wiki.preprocessors.html

@Suppress("EqualsOrHashCode")
class Import(val qualifiedName: String) {
    override fun equals(other: Any?): Boolean {
        other ?: return false
        if (other !is Import) return false
        return this.qualifiedName == other.qualifiedName
    }

    override fun toString(): String = "<import: ${this.qualifiedName}>"
}