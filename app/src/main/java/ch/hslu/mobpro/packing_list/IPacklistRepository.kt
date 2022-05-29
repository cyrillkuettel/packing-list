package ch.hslu.mobpro.packing_list

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.room.Packlist
import ch.hslu.mobpro.packing_list.room.PacklistWithItems
import kotlinx.coroutines.flow.Flow

/**
* This Interface provides means for the repository to have polymorphic manner.
* We can swap out the implementation without affecting callers. This can be
* useful for certain scenarios like unit tests. It's just a nice way of decoupling the
* PacklistRepository class from the rest of the system.
*/
interface IPacklistRepository {

    val allPacklists: Flow<List<Packlist>>


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertPacklist(packList: Packlist)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertItem(item: Item)
    fun getPackListWithItems(id: String): LiveData<List<PacklistWithItems>>
    fun getPackListByTitle(title: String): LiveData<List<Packlist>>

    @WorkerThread
    suspend fun existsByPacklist(id: Int): LiveData<Boolean>
    fun getStatus(itemContentID: Long): LiveData<List<Item>>
    suspend fun setStatus(itemContentID: Long, status: Boolean)
    suspend fun deleteItem(itemContentID: Long)
    suspend fun deleteItemsWithPacklist(title: String)
}