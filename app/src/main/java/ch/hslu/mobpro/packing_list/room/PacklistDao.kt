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

    @Query("SELECT * FROM packlist_table AS packlistitem WHERE packlistitem.id LIKE :title LIMIT :limit")
    fun getPacklistByTitle(title: String, limit: Int = 1): LiveData<List<Packlist>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(testItem: Item)

    @Transaction
    @Query("SELECT * FROM packlist_table WHERE id IN (SELECT DISTINCT(item_id) FROM item_table) AND id = :id")
    fun getItemsFromParentById(id: String) : LiveData<List<PacklistWithItems>>


    @Query("SELECT * FROM item_table AS item WHERE item.id = :itemContentID")
    fun getItemStatus(itemContentID: Long): LiveData<List<Item>>

    // NOTE: could also create a new item object and use the @Insert annotation.
    // However, this way I think, expresses more clearly the intent of the function
    @Query("UPDATE item_table SET status = :status WHERE id = :itemContentID")
    suspend fun setStatus(itemContentID: Long, status: Boolean)

    @Query("DELETE FROM item_table WHERE id = :itemContentID")
    suspend fun deleteByItemId(itemContentID: Long)


}