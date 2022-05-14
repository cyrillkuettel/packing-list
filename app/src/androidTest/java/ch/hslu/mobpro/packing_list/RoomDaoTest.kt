package ch.hslu.mobpro.packing_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.room.PacklistDao
import ch.hslu.mobpro.packing_list.room.PacklistRoomDatabase
import ch.hslu.mobpro.packing_list.room.PacklistWithItems
import ch.hslu.mobpro.packing_list.utilities.testPacklist
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
        val result = packLists.take(1).toList().get(0).get(0)
        assertEquals(testPacklist,  result)
    }

    @Test
    fun insertItemsAndretriveThemBack() = runBlocking  {
        val referenceId = testPacklist.title
        val item1 = Item(referenceId,"test")
        packlistDao.insertItem(item1)

        val queryResult: PacklistWithItems = packlistDao.getItemsFromParentById(
            referenceId).take(1).toList()[0][0]

        val retrivedItems =queryResult.items.contains(item1)
        assertTrue(retrivedItems)
    }

}
