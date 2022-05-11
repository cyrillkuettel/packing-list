package ch.hslu.mobpro.packing_list

import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow


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
        Log.v("PacklistRepository", "suspend fun insert")
        packlistDao.insert(word)
    }
}