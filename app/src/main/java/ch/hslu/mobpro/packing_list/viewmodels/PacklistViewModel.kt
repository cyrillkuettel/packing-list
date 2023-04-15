package ch.hslu.mobpro.packing_list.viewmodels


import androidx.lifecycle.*
import ch.hslu.mobpro.packing_list.IPacklistRepository
import ch.hslu.mobpro.packing_list.PacklistRepository
import ch.hslu.mobpro.packing_list.room.Packlist
import kotlinx.coroutines.launch


class PacklistViewModel(private val repository: IPacklistRepository) : ViewModel() {

    // Using LiveData and caching what all Packlists returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allPacklists: LiveData<List<Packlist>> = repository.allPacklists.asLiveData()


    val _navigateBacktoMenu: MutableLiveData<Boolean> = MutableLiveData()

    /** This flag is used to prevent MenuFragment from automatically navigating
     * to the clicked Packlist, if we are returning from that clicked Packlist. (By navigate up) */
    val _packListHasBeenClicked: MutableLiveData<Boolean> = MutableLiveData()


    fun insertNewPacklist(packlist: Packlist) = viewModelScope.launch {
       repository.insertPacklist(packlist)
        _navigateBacktoMenu.postValue(true)
    }

    /**
     * Used to determine which packlist has been selected in the MenuFragment
     */
    private val clickedPacklist: MutableLiveData<Packlist?> = MutableLiveData()


    fun setClickedPacklist(packlist: Packlist) = viewModelScope.launch{
        clickedPacklist.value = packlist
        // XXX Set to null again, to prevent cycling back to where we came from:
        clickedPacklist.value = null
    }

    fun getClickedPacklist(): MutableLiveData<Packlist?> {
        return clickedPacklist
    }

    fun setPackListHasBeenClicked(value: Boolean) {
       _packListHasBeenClicked.postValue(value)
    }

    fun deletePacklist(title: String) = viewModelScope.launch {
        repository.deleteItemsWithPacklist(title)
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

