package ch.hslu.mobpro.packing_list

import android.app.Application
import ch.hslu.mobpro.packing_list.room.PacklistRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class PacklistApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { PacklistRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { PacklistRepository(database.packListDao()) }
}
