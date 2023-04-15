package ch.hslu.mobpro.packing_list

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.room.Packlist
import ch.hslu.mobpro.packing_list.room.PacklistDao
import ch.hslu.mobpro.packing_list.room.PacklistWithItems
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*


/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 *
 * All information flows through this repository, regardless of the content.
 */
class PacklistRepository(private val packlistDao: PacklistDao,
                         private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : IPacklistRepository {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    override val allPacklists: Flow<List<Packlist>> = packlistDao.getAlphabetizedPacklist()



    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @WorkerThread
    override suspend fun insertPacklist(packList: Packlist)  {
        Log.v(TAG, "repository insert")
        packlistDao.insertPacklist(packList)
    }


    @WorkerThread
    override suspend fun insertItem(item: Item)  {
        packlistDao.insertItem(item)
    }

    override fun getPackListWithItemsByUUID(id: String) : LiveData<List<PacklistWithItems>> {
        return packlistDao.getItemsFromParentByUUID(UUID.fromString(id))
    }

    override fun getPackListByUUID(uuid: String) : LiveData<List<Packlist>> {
        return packlistDao.getPackListByUUID(UUID.fromString(uuid))
    }


    override fun getStatus(itemContentID: Long): LiveData<List<Item>> {
        return packlistDao.getItemStatus(itemContentID)
    }

    override suspend fun deleteItem(itemContentID: Long) {
        withContext(ioDispatcher) {
            packlistDao.deleteByItemId(itemContentID)
        }
    }

    /** if packlist does not have items, the method to delete it is slightly different */
    override suspend fun deleteItemsWithPacklist(uuid: UUID) {
        withContext(ioDispatcher) {
            val packListContainsItems: Boolean = packlistDao.packListContainsItems(uuid)
            if (packListContainsItems) {
                Log.d(TAG, "packListContainsItems,  so delete Every")
                packlistDao.deleteItemWithPackList(uuid)
            } else {
                Log.d(TAG, "packlist does not contain any items, so we only delete the packlist")
                packlistDao.deletePackListByTitle(uuid.toString())
            }
        }
    }

    override suspend fun updateTitle(oldTitle: String, newTitle: String) {
        withContext(ioDispatcher) {
            packlistDao.updateTitle(oldTitle, newTitle)
        }
    }

    companion object {
        const val TAG = "PacklistRepository"
    }

}