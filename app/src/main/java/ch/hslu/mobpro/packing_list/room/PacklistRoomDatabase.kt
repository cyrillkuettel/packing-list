package ch.hslu.mobpro.packing_list.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(entities = [Packlist::class, Item::class], version = 4, exportSchema = false)
abstract class PacklistRoomDatabase : RoomDatabase() {

    abstract fun packListDao(): PacklistDao


    private class PacklistCallback(
        private val scope: CoroutineScope,
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val packlistDao = database.packListDao()
                    packlistDao.deleteAll()

                    // Populate with some sample packlist, for testing.
                    val word = Packlist("Hello","@Home", "09.12.1999")
                    packlistDao.insert(word)

                }
            }
        }
    }

    companion object {
        private const val TAG = "PlantRoomDatabase"

        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PacklistRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope,
        ):
                PacklistRoomDatabase {

            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            Log.v(TAG, "getDatabase")

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PacklistRoomDatabase::class.java,
                    "word_database"
                ).addCallback(PacklistCallback(scope)).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
