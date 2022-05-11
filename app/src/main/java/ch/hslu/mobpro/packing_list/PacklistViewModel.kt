package ch.hslu.mobpro.packing_list



import kotlinx.coroutines.launch
import androidx.lifecycle.*


class PacklistViewModel(private val repository: PacklistRepository) : ViewModel() {

    // Using LiveData and caching what allPacklists returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allPacklists: LiveData<List<Packlist>> = repository.allPacklists.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(packlist: Packlist) = viewModelScope.launch {
        repository.insert(packlist)
    }
}

class PacklistViewModelFactory(private val repository: PacklistRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PacklistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PacklistViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
