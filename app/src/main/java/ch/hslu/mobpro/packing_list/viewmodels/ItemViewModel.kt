package ch.hslu.mobpro.packing_list.viewmodels

import androidx.lifecycle.*
import ch.hslu.mobpro.packing_list.PacklistRepository
import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.room.Packlist
import ch.hslu.mobpro.packing_list.room.PacklistWithItems
import kotlinx.coroutines.launch

class ItemViewModel(private val repository: PacklistRepository) : ViewModel() {

    /** The Packlist whose items are currently displayed */
    private var currentEditingPacklist: LiveData<List<Packlist>> = MutableLiveData()

    /** Boolean flag which triggers if a new Item has been submitted in [CreateItemFragment] */
    val _navigateBackToItemOverview: MutableLiveData<Boolean> = MutableLiveData()


    fun getCurrentEditingPackList() : LiveData<List<Packlist>> {
        return currentEditingPacklist
    }

    fun setCurrentEditingPacklistfromUUID(uuid: String) {
        currentEditingPacklist = repository.getPackListByUUID(uuid)
    }

    fun insertNewItem(item: Item) = viewModelScope.launch{
        repository.insertItem(item)
        _navigateBackToItemOverview.postValue(true)
    }

    fun updateTitle(oldTitle: String, newTitle: String) = viewModelScope.launch{
        repository.updateTitle(oldTitle, newTitle)
    }

    fun getPackListWithItemsByUUID(id: String) : LiveData<List<PacklistWithItems>> {
        return repository.getPackListWithItemsByUUID(id)
    }

    fun delete(itemContentID: Long)  = viewModelScope.launch {
        repository.deleteItem(itemContentID)
    }

}



class ItemViewModelFactory(private val repository: PacklistRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}