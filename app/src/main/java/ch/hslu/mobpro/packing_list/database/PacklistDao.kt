package ch.hslu.mobpro.packing_list.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * The Room Magic is in this file, where you map a method call to an SQL query.
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */

@Dao
interface PacklistDao {

    @Query("SELECT * FROM packlist_table ORDER BY packlist ASC")
    fun getAlphabetizedPacklist(): Flow<List<Packlist>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(packlist: Packlist): Long

    @Query("DELETE FROM packlist_table")
    suspend fun deleteAll()

    @Query("SELECT EXISTS (SELECT 1 FROM packlist_table WHERE id = :id)")
    fun existsByPacklist(id: Int): Boolean

    @Query("SELECT * FROM packlist_table AS item WHERE item.packlist LIKE :title LIMIT :limit")
    fun getPacklistByTitle(title: String, limit: Int = 1): LiveData<List<Packlist>>
}