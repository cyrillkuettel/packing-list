package ch.hslu.mobpro.packing_list.room

import androidx.room.Embedded
import androidx.room.Relation


data class PacklistWithItems(
    @Embedded val packlist: Packlist,
    @Relation(
        parentColumn = "id",
        entityColumn = "itemCreatorID"
    )
    val items: List<Item> = emptyList()
)
