package ch.hslu.mobpro.packing_list.viewmodels

import androidx.lifecycle.*
import ch.hslu.mobpro.packing_list.PacklistRepository
import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.room.Packlist
import kotlinx.coroutines.launch

class ItemViewModel(private val repository: PacklistRepository) : ViewModel() {


    private var currentEditingPacklist: LiveData<List<Packlist>> = MutableLiveData()

    val _navigateBacktoPacklist: MutableLiveData<Boolean> = MutableLiveData()



    fun getCurrentEditingPackList() : LiveData<List<Packlist>> {
        return currentEditingPacklist
    }

    fun setCurrentEditingPackListTitle(title: String) {
        currentEditingPacklist = repository.getPackListByTitle(title)
    }

    fun insertNewItem(item: Item) = viewModelScope.launch{
        repository.insertItem(item)
        _navigateBacktoPacklist.postValue(true)
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