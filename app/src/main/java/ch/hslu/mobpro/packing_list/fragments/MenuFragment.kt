package ch.hslu.mobpro.packing_list.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ch.hslu.mobpro.packing_list.*
import ch.hslu.mobpro.packing_list.databinding.FragmentMenuBinding
import ch.hslu.mobpro.packing_list.viewmodels.PacklistViewModel
import ch.hslu.mobpro.packing_list.viewmodels.PacklistViewModelFactory

/**
 * A fragment representing a list of packing lists.
 * This is the first fragment that is shown in the App.
 */
class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private val packlistViewModel: PacklistViewModel by viewModels {
        PacklistViewModelFactory((requireActivity().application as PacklistApplication).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = setupRecyclerView()
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_MenuFragment_to_CreateListFragment)
        }
        observeViewModels(adapter)
    }

    private fun setupRecyclerView(): PackListAdapter {
        val recyclerView = binding.recyclerView
        val adapter = PackListAdapter(packlistViewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return adapter
    }

    private fun observeViewModels(adapter: PackListAdapter) {
        // Add an observer on the LiveData returned by getAlphabetizedPacklist.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        packlistViewModel.allPacklists.observe(viewLifecycleOwner) { newPacklist ->
            newPacklist.let { adapter.submitList(it) }
        }

        packlistViewModel.getClickedPacklist().observe(viewLifecycleOwner) { packList ->
            // we assume title is unique
            val action = packList?.let {
                Log.v(TAG, "setting Title as argument: title is ${it.title}")
                MenuFragmentDirections.actionMenuFragmentToPacklistFragment(it.title)
            }
            if (action != null) {
                findNavController().navigate(action)
            } else {
                Log.e(TAG, "NavDirections action is null")
            }
        }
    }

    companion object {
        private const val TAG = "MenuFragment"
    }
}