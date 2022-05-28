package ch.hslu.mobpro.packing_list.settings

import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragmentCompat
import ch.hslu.mobpro.packing_list.R

class ListPreferencesFragment: PreferenceFragmentCompat()  {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Log.v(TAG, "onCreatePreferences")
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    companion object {
        fun newInstance(): ListPreferencesFragment {
            return ListPreferencesFragment()
        }
        const val TAG = "PreferenceFragment"
    }
}