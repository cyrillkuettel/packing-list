package ch.hslu.mobpro.packing_list.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "item_table",
foreignKeys = [
            androidx.room.ForeignKey(entity = Packlist::class,
                parentColumns = ["id"],
                childColumns = ["item_id"])
    ],
    indices = [Index("item_id")]
)
data class Item(
    @ColumnInfo(name = "item_id")
    val itemId: String, // to reference Parent
    val content: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var itemContentID: Long = 0


    override fun toString() =
        """
         Item
        itemContentID: $itemContentID
        itemId: $itemId
        content: $content
    """.trimIndent()

}