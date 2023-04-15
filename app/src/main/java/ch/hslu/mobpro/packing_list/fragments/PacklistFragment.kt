package ch.hslu.mobpro.packing_list.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ch.hslu.mobpro.packing_list.*
import ch.hslu.mobpro.packing_list.adapter.ItemAdapter
import ch.hslu.mobpro.packing_list.databinding.FragmentPacklistBinding
import ch.hslu.mobpro.packing_list.settings.SharedPreferencesViewModel
import ch.hslu.mobpro.packing_list.viewmodels.ItemViewModel
import ch.hslu.mobpro.packing_list.viewmodels.ItemViewModelFactory

/**
 * Single Top-Level List.
 */
class PacklistFragment : Fragment() {

    private val args: PacklistFragmentArgs by navArgs()

    private val itemViewModel: ItemViewModel by viewModels {
        ItemViewModelFactory((requireActivity().application as PacklistApplication).repository)
    }

    private val sharedPreferencesViewModel: SharedPreferencesViewModel by activityViewModels()

    private var _binding: FragmentPacklistBinding? = null
    private val binding get() = _binding!!

    /** References the currently selected item */
    private var currentPackListTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /** This ensures we can use the back button to return to MenuFragment */
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_PacklistFragment_to_MenuFragment)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPacklistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = args.title // retrieve title from Arguments, this uniquely identifies Packlist
        Log.v(TAG, "receiving arguments, args.title is $title")
        itemViewModel.setCurrentEditingPackListTitle(title)
        currentPackListTitle = title
        val adapter = setupRecyclerView()
        observeViewModels(adapter)

        binding.fabCreateNewNote.setOnClickListener {
            navigateToCreateItemFragment()
        }

        setupSwipeToDeleteItems(adapter)
    }

    private fun setupSwipeToDeleteItems(adapter: ItemAdapter) {
        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val itemToDelete = adapter.getItemAt(viewHolder.adapterPosition)?.itemContentID
                    itemToDelete?.let {
                        Log.d(TAG, "deleting item $it")
                        itemViewModel.delete(it)
                    }
                }
            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.itemRecyclerView)
    }


    private fun navigateToCreateItemFragment() {
        val action = currentPackListTitle?.let {
            PacklistFragmentDirections.actionPacklistFragmentToCreateItemFragment(it)
        }
        action?.let {
            findNavController().navigate(it)
        }
    }

    private fun setupRecyclerView(): ItemAdapter {
        val recyclerView = binding.itemRecyclerView
        val lifeCycleOwner = viewLifecycleOwner
        val adapter = ItemAdapter(itemViewModel, lifeCycleOwner)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), NUMBER_OF_COLUMNS)
        return adapter
    }


    private fun observeViewModels(adapter: ItemAdapter) {
        Log.v(TAG, "observeViewModels")

        // Get Title
        itemViewModel.getCurrentEditingPackList()
            .observe(viewLifecycleOwner) { matchingTitlePacklist ->
                Log.v(TAG, "successfully retrieved matchingTitlePacklist")
                currentPackListTitle = matchingTitlePacklist[0].title
            }


        // Set Title // Get Items
        currentPackListTitle?.let { title ->
            binding.textViewPacklistTitle.text = currentPackListTitle

            /** Items get automatically refreshed if once item has been deleted.  */
            itemViewModel.getPackListWithItems(title).observe(viewLifecycleOwner) { items ->
                Log.v(TAG, "itemViewModel.getItems(it).observe")
                if (items.isNotEmpty()) { // List can have size 0 if no items have been created yet
                    val packlistWithItems = items[0] // There will only ever be exactly one Element
                    val itemList = packlistWithItems.items
                    itemList.let { adapter.submitList(it) } // Adapter will take care of the rest.
                } else {
                    // This is important, for example in the case where there is one item and then
                    // the user deletes it.
                    adapter.submitList(emptyList())
                }
            }
        }

        /** Columns can be changed dynamically in preferences */
        sharedPreferencesViewModel.getCurrentColumns().observe(viewLifecycleOwner) { cols ->
            Log.d(TAG, "getPreferencesSummary: columns $cols")
            binding.itemRecyclerView.layoutManager = GridLayoutManager(requireContext(), cols)
        }

    }


    override fun onResume() {
        super.onResume()
        /** Because Preferences can be changed at any time, we check here to see if it the value changed */
        sharedPreferencesViewModel.buildColumnPreferences()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val NUMBER_OF_COLUMNS = 2
        private const val TAG = "PacklistFragment"
    }

}