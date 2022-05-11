package ch.hslu.mobpro.packing_list

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ch.hslu.mobpro.packing_list.database.Packlist
import ch.hslu.mobpro.packing_list.database.PacklistDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */
class PacklistRepository(private val packlistDao: PacklistDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allPacklists: Flow<List<Packlist>> = packlistDao.getAlphabetizedPacklist()


    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Packlist) {
        packlistDao.insert(word)
    }


    @WorkerThread
    fun existsByPacklist(id: Int) : LiveData<Boolean> {
        val data: MutableLiveData<Boolean> = MutableLiveData()
        CoroutineScope(Dispatchers.IO).launch {
            val exists: Boolean = packlistDao.existsByPacklist(id)
            data.postValue(exists)
        }
        return data
    }


}