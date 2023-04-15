package ch.hslu.mobpro.packing_list.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.hslu.mobpro.packing_list.PacklistApplication
import ch.hslu.mobpro.packing_list.R
import ch.hslu.mobpro.packing_list.adapter.PackListAdapter
import ch.hslu.mobpro.packing_list.databinding.FragmentMenuBinding
import ch.hslu.mobpro.packing_list.viewmodels.PacklistViewModel
import ch.hslu.mobpro.packing_list.viewmodels.PacklistViewModelFactory
import java.util.*

/**
 * This is the first fragment that is shown in the App.
 * A fragment representing a list of packing lists.
 */
class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    internal val packlistViewModel: PacklistViewModel by viewModels {
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
        Log.d(TAG, "MMenuFrafgment onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        val adapter = setupRecyclerView()
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_MenuFragment_to_CreateListFragment)
        }
        setupSwipeToDeletePacklists(adapter)
        observeViewModels(adapter)
    }

    private fun setupRecyclerView(): PackListAdapter {
        val recyclerView = binding.packlistRecyclerView
        val adapter = PackListAdapter(packlistViewModel, requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return adapter
    }

    private fun setupSwipeToDeletePacklists(adapter: PackListAdapter) {
        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val packListToDelete = adapter.getItemAt(viewHolder.adapterPosition)!!.id
                    packListToDelete.let {
                        packlistViewModel.deletePacklist(it)
                    }
                }
            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.packlistRecyclerView)
    }


    private fun observeViewModels(adapter: PackListAdapter) {

        packlistViewModel.allPacklists.observe(viewLifecycleOwner) { newPacklist ->
            newPacklist.let { adapter.submitList(it) }
        }

        packlistViewModel.getClickedPacklist().observe(viewLifecycleOwner) { packList ->
                val action = packList?.let {
                    MenuFragmentDirections.actionMenuFragmentToPacklistFragment(it.id.toString())
                }
                /* If action is not null, the navigate function is called on it.
                 If action is null, nothing happens. */
                action?.let(findNavController()::navigate)
        }
    }

    companion object {
        private const val TAG = "MenuFragment"
    }
}