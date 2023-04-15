package ch.hslu.mobpro.packing_list.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ch.hslu.mobpro.packing_list.utils.Colors.defaultNoteColor


@Entity(tableName = "packlist_table")
data class Packlist(
    @PrimaryKey @ColumnInfo(name = "id") val title: String,
    val location: String,
    val content: String,
    val color: Int
) {

    // Secondary constructor for convenience
    constructor(title: String) :
            this(title, "", "", defaultNoteColor)


    constructor(title: String, color: Int) :
            this(title, "", "", color)
}