package ch.hslu.mobpro.packing_list


import androidx.lifecycle.*
import ch.hslu.mobpro.packing_list.database.Packlist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PacklistViewModel(private val repository: PacklistRepository) : ViewModel() {

    // Using LiveData and caching what all Packlists returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allPacklists: LiveData<List<Packlist>> = repository.allPacklists.asLiveData()

    private var currentEditingPacklist: LiveData<List<Packlist>> = MutableLiveData()

    /** used for existence check before submitting a new list */
    var checkCurrentPackList: LiveData<Boolean>? = null



    /**
     * Used to determine which packlist has been selected in the
     */
    private val clickedPacklist: MutableLiveData<Packlist?> = MutableLiveData()

    /**
     * Returns an id of the inserted packlist
     */
    suspend fun insertPacklist(packlist: Packlist) : Long  {
        return repository.insert(packlist)
    }

    fun getCurrentEditingPackList() : LiveData<List<Packlist>> {
        return currentEditingPacklist
    }

    fun setCurrentEditingPackListTitle(title: String) {
        currentEditingPacklist = repository.getPackListByTitle(title)
    }




    fun startExistenceCheck(packlist: Packlist) {
        // TODO: update the currentPacklist in database so that it can be accessed from the next fragment
        checkCurrentPackList = repository.existsByPacklist(packlist.id)
    }

    fun setClickedPacklist(packlist: Packlist) = viewModelScope.launch{

        clickedPacklist.value = packlist
    }

    fun getClickedPacklist(): MutableLiveData<Packlist?> {
        return clickedPacklist
    }


}

class PacklistViewModelFactory(private val repository: PacklistRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PacklistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PacklistViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}