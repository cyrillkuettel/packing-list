package ch.hslu.mobpro.packing_list.utilities

object Util {
    fun <T> Collection<T>?.assertEmpty() {
        require(this.isNullOrEmpty()) { "Expected an empty collection but was: $this" }
    }
}