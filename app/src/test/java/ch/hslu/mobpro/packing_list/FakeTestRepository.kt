package ch.hslu.mobpro.packing_list

import androidx.lifecycle.LiveData
import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.room.Packlist
import ch.hslu.mobpro.packing_list.room.PacklistWithItems
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.util.*


/**
 * Implementation of a data source with static access to the data for easy testing.
 */
class FakeTestRepository : IPacklistRepository {

    val packlistData: LinkedHashMap<String, Packlist> = LinkedHashMap()

    override var allPacklists: Flow<List<Packlist>> = emptyFlow()

    fun insertPacklists(vararg packList: Packlist) {
        for (list in packList) {
            packlistData[list.title] = list
        }
        runBlocking { refreshPacklist() }
    }

    override suspend fun insertPacklist(packList: Packlist) {
        packlistData[packList.title] = packList
        refreshPacklist()
    }

    private fun refreshPacklist() {
        allPacklists = getPackList()

    }

    private fun getPackList(): Flow<List<Packlist>> {
        val list = packlistData.map { it.value }.toList()
        val flowOfList = list.asFlow().toSingleListItem()
        return flowOfList
    }

    /** Helper function to simply transform Flow<T> to Flow<List<T>> */
     private fun <T> Flow<T>.toSingleListItem(): Flow<List<T>> = flow {
        val list = toList(mutableListOf())
        emit(list)
    }

    override fun getPackListByUUID(uuid: String): LiveData<List<Packlist>> {
        TODO("Not yet implemented")
    }


    override fun getStatus(itemContentID: Long): LiveData<List<Item>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteItem(itemContentID: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteItemsWithPacklist(uuid: UUID) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTitle(oldTitle: String, newTitle: String) {
        TODO("Not yet implemented")
    }


    /** item specific functions */
    override suspend fun insertItem(item: Item) {
        TODO("Not yet implemented")
    }

    override fun getPackListWithItemsByUUID(id: String): LiveData<List<PacklistWithItems>> {
        TODO("Not yet implemented")
    }


}
