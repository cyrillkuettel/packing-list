package ch.hslu.mobpro.packing_list.room

import androidx.room.Embedded
import androidx.room.Relation


data class PacklistWithItems(
    @Embedded val packlist: Packlist,
    @Relation(
        parentColumn = "id",
        entityColumn = "item_id"
    )
    val items: List<Item> = emptyList()


) {
    override fun toString() =
        """
        PacklistWithItems packlist: $packlist items: $items
    """.trimIndent()

}
