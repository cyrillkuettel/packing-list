package ch.hslu.mobpro.packing_list

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.room.Packlist
import ch.hslu.mobpro.packing_list.room.PacklistWithItems
import kotlinx.coroutines.flow.Flow

interface IPacklistRepository {

    val allPacklists: Flow<List<Packlist>>


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertPacklist(packList: Packlist)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertItem(item: Item)
    fun getItems(id: String): LiveData<List<PacklistWithItems>>
    fun getPackListByTitle(title: String): LiveData<List<Packlist>>
    fun updateItems(items: LiveData<List<PacklistWithItems>>)

    @WorkerThread
    suspend fun existsByPacklist(id: Int): LiveData<Boolean>
    fun getStatus(itemContentID: Long): LiveData<List<Item>>
    suspend fun setStatus(itemContentID: Long, status: Boolean)
    suspend fun delete(itemContentID: Long)
}