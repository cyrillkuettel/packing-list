package ch.hslu.mobpro.packing_list.room

import android.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ch.hslu.mobpro.packing_list.utils.colors
import ch.hslu.mobpro.packing_list.utils.colors.noteDefaultColor


@Entity(tableName = "packlist_table")
data class Packlist(
    @PrimaryKey @ColumnInfo(name = "id") val title: String,
    val location: String,
    val content: String,
    val color: Int
) {

    // Secondary constructor for convenience
    constructor(title: String) :
            this(title, "", "", noteDefaultColor.toColorInt())
}