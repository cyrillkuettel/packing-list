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

    /** used for existence check before submitting a new list */
    var checkCurrentPackList: LiveData<Boolean>? = null


    private val clickedPacklist: MutableLiveData<Packlist?> = MutableLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(packlist: Packlist) = viewModelScope.launch {
        repository.insert(packlist)
    }


    fun startExistenceCheck(packlist: Packlist) {
        checkCurrentPackList = repository.existsByPacklist(packlist.id)
    }

    fun setClickedPacklist(packlist: Packlist) {
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