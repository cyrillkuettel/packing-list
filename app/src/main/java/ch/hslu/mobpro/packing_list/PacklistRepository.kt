package ch.hslu.mobpro.packing_list

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.room.Packlist
import ch.hslu.mobpro.packing_list.room.PacklistDao
import ch.hslu.mobpro.packing_list.room.PacklistWithItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 *
 * The Interface [IPacklistRepository] enables swapping out the implementation, this can be
 * useful for certain scenarios, for example, unit tests.
 */
class PacklistRepository(private val packlistDao: PacklistDao) : IPacklistRepository {

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





    @WorkerThread
    override fun existsByPacklist(id: Int): LiveData<Boolean> {
        val data: MutableLiveData<Boolean> = MutableLiveData()
        CoroutineScope(Dispatchers.IO).launch {
            val exists: Boolean = packlistDao.existsByPacklist(id)
            data.postValue(exists)
        }
        return data
    }

    companion object {
        const val TAG = "PacklistRepository"
    }

}