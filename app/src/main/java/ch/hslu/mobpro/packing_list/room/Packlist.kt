package ch.hslu.mobpro.packing_list.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "packlist_table")
data class Packlist(
    @PrimaryKey @ColumnInfo(name = "id") val title: String,
    val location: String,
    val date: String,
    val color: Int
)
//     val color: String