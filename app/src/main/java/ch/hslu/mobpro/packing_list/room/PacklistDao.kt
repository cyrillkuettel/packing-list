package ch.hslu.mobpro.packing_list.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

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
    suspend fun insertPacklist(packlist: Packlist)

    @Query("DELETE FROM packlist_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM packlist_table AS packlistitem WHERE packlistitem.id LIKE :id LIMIT :limit")
    fun getPackListByUUID(id: UUID, limit: Int = 1): LiveData<List<Packlist>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(testItem: Item)

    @Transaction
    @Query("SELECT * FROM packlist_table WHERE id IN (SELECT DISTINCT(item_id) FROM item_table) AND id = :id")
    fun getItemsFromParentByUUID(id: UUID) : LiveData<List<PacklistWithItems>>


    @Query("SELECT * FROM item_table AS item WHERE item.id = :itemContentID")
    fun getItemStatus(itemContentID: Long): LiveData<List<Item>>


    @Query("DELETE FROM item_table WHERE id = :itemContentID")
    suspend fun deleteByItemId(itemContentID: Long)

    @Query("DELETE FROM packlist_table WHERE title = :title")
    suspend fun deletePackListByTitle(title: String)

    /** This deletes a one to many relationship.
     * It can be used to delete a packlist and all it's items. */
    @Transaction
    @Query("DELETE FROM packlist_table WHERE id IN (SELECT DISTINCT(item_id) FROM item_table) AND id = :id")
    suspend fun deleteItemWithPackList(id: UUID)

    /** Returns true if the Packlist has any items associated with it, False otherwise. This method
     * is used to determine what method to use to delete packlist. */
    @Transaction
    @Query("SELECT EXISTS (SELECT 1 FROM packlist_table WHERE id IN (SELECT DISTINCT(item_id) FROM item_table) AND id = :id)")
    fun packListContainsItems(id: UUID) : Boolean


    @Transaction
    @Query("UPDATE packlist_table SET title = :newTitle WHERE title = :oldTitle")
    suspend fun updateTitle(oldTitle: String, newTitle: String)


}