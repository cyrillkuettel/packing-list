package ch.hslu.mobpro.packing_list.settings


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager

class SharedPreferencesViewModel(application: Application) : AndroidViewModel(application) {

    private val COLUMN_DEFAULT_NUMBER: String = "2"
    private val NUMBER_OF_COLUMNS: String = "numberOfColumns"

    // observable data holder class. LiveData is lifecycle-aware
    private var preferencesColumns: MutableLiveData<Int> = MutableLiveData()

    fun getCurrentColumns(): LiveData<Int> {
        return preferencesColumns
    }

    fun setDefaultPreferences() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(getApplication<Application>().applicationContext)
        val editor = prefs.edit()
        editor.putString(NUMBER_OF_COLUMNS, COLUMN_DEFAULT_NUMBER)
        editor.apply()
        buildColumnPreferences()
    }

    fun buildColumnPreferences() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(getApplication<Application>().applicationContext)
        val numberOfColumns = prefs.getString(NUMBER_OF_COLUMNS, COLUMN_DEFAULT_NUMBER)
        numberOfColumns?.toInt()?.also { preferencesColumns.value = it }
    }





}