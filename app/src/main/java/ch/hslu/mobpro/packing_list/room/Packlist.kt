package ch.hslu.mobpro.packing_list.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "packlist_table")
data class Packlist(
    @ColumnInfo(name = "title") val title: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
