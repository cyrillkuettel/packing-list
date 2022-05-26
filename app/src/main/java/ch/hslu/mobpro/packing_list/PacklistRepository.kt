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
 * The Interface [IPacklistRepository] enables swapping out the implementation, this can be
 * useful for certain scenarios, for example, unit tests.
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

    override fun getItems(id: String) : LiveData<List<PacklistWithItems>> {
        return packlistDao.getItemsFromParentById(id)
    }

    override fun getPackListByTitle(title: String) : LiveData<List<Packlist>> {

        return packlistDao.getPacklistByTitle(title)
    }

    override fun updateItems(items: LiveData<List<PacklistWithItems>>){
     //   packlistDao.updateItems(items)
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

    override suspend fun setStatus(itemContentID: Long, status: Boolean) {
        withContext(ioDispatcher) {
            packlistDao.setStatus(itemContentID, status)
        }
    }

    override suspend fun delete(itemContentID: Long) {
        withContext(ioDispatcher) {
            packlistDao.deleteByItemId(itemContentID)
        }
    }

    companion object {
        const val TAG = "PacklistRepository"
    }

}