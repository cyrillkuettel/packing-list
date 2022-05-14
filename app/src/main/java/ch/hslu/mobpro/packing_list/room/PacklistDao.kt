package ch.hslu.mobpro.packing_list.room

import androidx.lifecycle.LiveData
import androidx.room.*
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

    @Query("SELECT * FROM packlist_table")
    fun getAlphabetizedPacklist(): Flow<List<Packlist>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(packlist: Packlist)

    @Query("DELETE FROM packlist_table")
    suspend fun deleteAll()

    @Query("SELECT EXISTS (SELECT 1 FROM packlist_table WHERE id = :id)")
    fun existsByPacklist(id: Int): Boolean

    @Query("SELECT * FROM packlist_table AS item WHERE item.title LIKE :title LIMIT :limit")
    fun getPacklistByTitle(title: String, limit: Int = 1): LiveData<List<Packlist>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItem(testItem: Item)

    @Query("SELECT * FROM item_table")
    fun getAllItems() : Flow<List<Item>>

    @Transaction
    @Query("SELECT * FROM packlist_table WHERE id IN (SELECT DISTINCT(itemContentID) FROM item_table) AND title LIKE :title ")
    fun getPackListWithItems(title: String) : Flow<List<PacklistWithItems>>


}