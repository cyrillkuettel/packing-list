package ch.hslu.mobpro.packing_list

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "packlist_table")
data class Packlist(@PrimaryKey @ColumnInfo(name = "packlist") val title: String) // TODO: add more attributes
