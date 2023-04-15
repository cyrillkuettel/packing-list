package ch.hslu.mobpro.packing_list

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.room.Packlist
import ch.hslu.mobpro.packing_list.room.PacklistDao
import ch.hslu.mobpro.packing_list.room.PacklistWithItems
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow


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
        packlistDao.insert(packList)
    }


    @WorkerThread
    override suspend fun insertItem(item: Item)  {
        packlistDao.insertItem(item)
    }

    override fun getPackListWithItems(id: String) : LiveData<List<PacklistWithItems>> {
        return packlistDao.getItemsFromParentById(id)
    }

    override fun getPackListByTitle(title: String) : LiveData<List<Packlist>> {
        return packlistDao.getPacklistByTitle(title)
    }


    @WorkerThread
    override suspend fun existsByPacklist(id: Int): LiveData<Boolean> {
        val data: MutableLiveData<Boolean> = MutableLiveData()
        withContext(ioDispatcher) {
            val exists: Boolean = packlistDao.existsByPacklist(id)
            data.postValue(exists)
        }
        return data
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
    override suspend fun deleteItemsWithPacklist(title: String) {
        withContext(ioDispatcher) {
            val packListContainsItems: Boolean = packlistDao.packListContainsItems(title)
            if (packListContainsItems) {
                Log.d(TAG, "packListContainsItems,  so delete Every")
                packlistDao.deleteItemWithPackList(title)
            } else {
                Log.d(TAG, "packlist does not contain any items, so we only delete the packlist")
                packlistDao.deletePacklistById(title)
            }
        }
    }

    companion object {
        const val TAG = "PacklistRepository"
    }

}