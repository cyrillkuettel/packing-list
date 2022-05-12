package ch.hslu.mobpro.packing_list.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "packlist_table")
data class Packlist(
    @ColumnInfo(name = "packlist") val title: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
