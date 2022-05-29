package ch.hslu.mobpro.packing_list

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.hslu.mobpro.packing_list.room.*
import ch.hslu.mobpro.packing_list.utilities.getOrAwaitValue
import ch.hslu.mobpro.packing_list.utilities.testPacklist
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomDaoTest {
    private lateinit var database: PacklistRoomDatabase
    private lateinit var packlistDao: PacklistDao


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, PacklistRoomDatabase::class.java).build()
        packlistDao = database.packListDao()
        packlistDao.insert(testPacklist)
    }

    @Throws(IOException::class)
    @After fun closeDb() {
        database.close()
    }

    @Test
    fun testTakeOutASinglePacklist() = runBlocking  {
        val packLists = packlistDao.getAlphabetizedPacklist()
        val result = packLists.take(1).toList()[0][0]
        assertEquals(testPacklist,  result)
    }

    @Test
    fun testInsertingItems_ShouldBeReturnedAsLiveData() = runBlocking  {

        val id = testPacklist.title
        // given a arbitrary item
        val testItemInserted = Item(id,"test", true, -13070788)
        // insert the item to database
            packlistDao.insertItem(testItemInserted)

        // retrieve this item from database, it should be equal to the original item
        val queryResult: List<PacklistWithItems> = packlistDao.getItemsFromParentById(
            id).getOrAwaitValue()
        val retrievedItems = queryResult[0].items.contains(testItemInserted)
        assertTrue(retrievedItems)
    }

    @Test
    fun testDeletingPackList() = runBlocking  {

        val id = testPacklist.title // packlist is inserted
        val testItemInserted1 = Item(id,"test1", true, -13070788)
        val testItemInserted2 = Item(id,"test2", true, -13070788)

        // Given some items inserted to database
        packlistDao.insertItem(testItemInserted1)
        packlistDao.insertItem(testItemInserted2)

        // retrieve the items from database, it should be equal to the original item
        val queryResult: List<PacklistWithItems> = packlistDao.getItemsFromParentById(
            id).getOrAwaitValue()
        val item = queryResult[0]
        Log.d(TAG, item.toString())
        assertTrue(queryResult[0].items.size == 2)

        // now delete the Items from he packlist table, this should delete everything
        packlistDao.deleteItemWithPackList(id)
        val queryResultAfterDelete: List<PacklistWithItems> = packlistDao.getItemsFromParentById(
            id).getOrAwaitValue()
        assertTrue(queryResultAfterDelete.isEmpty())

        // check if the parent has been deleted as well
        val testParentEntityDeleted: List<Packlist> = packlistDao.getPacklistByTitle(id).getOrAwaitValue()
        assertTrue(testParentEntityDeleted.isEmpty())
    }




    companion object {
        const val TAG = "RoomDaoTest"
    }

}
