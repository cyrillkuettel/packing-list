package ch.hslu.mobpro.packing_list.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "item_table",
        foreignKeys = [
            androidx.room.ForeignKey(entity = Packlist::class,
                parentColumns = ["id"],
                childColumns = ["item_id"],
                // The option onDelete is crucial, else we get SQLiteConstraintException:
                // FOREIGN KEY constraint failed (code 787 SQLITE_CONSTRAINT_FOREIGNKEY)
                onDelete = androidx.room.ForeignKey.CASCADE)
        ],
    indices = [Index("item_id")]
)
data class Item(
    @ColumnInfo(name = "item_id") val itemId: UUID,     // to reference Parent
    val content: String,    // TextView from Item
    val color: Int          // CardView background color
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var itemContentID: Long = 0 // internal id of item ?


    override fun toString() =
        """
        Item
        itemContentID: $itemContentID
        itemId: $itemId
        content: $content
    """.trimIndent()

}