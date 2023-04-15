package ch.hslu.mobpro.packing_list

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.room.Packlist
import ch.hslu.mobpro.packing_list.room.PacklistWithItems
import kotlinx.coroutines.flow.Flow
import java.util.*

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
    fun getPackListWithItemsByUUID(id: String): LiveData<List<PacklistWithItems>>
    fun getPackListByUUID(uuid: String): LiveData<List<Packlist>>

    fun getStatus(itemContentID: Long): LiveData<List<Item>>

    suspend fun deleteItem(itemContentID: Long)

    suspend fun deleteItemsWithPacklist(uuid: UUID)

    @WorkerThread
    suspend fun updateTitle(oldTitle: String, newTitle: String)
}