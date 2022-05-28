package ch.hslu.mobpro.packing_list.fragments

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
import ch.hslu.mobpro.packing_list.databinding.FragmentPacklistBinding
import ch.hslu.mobpro.packing_list.settings.SharedPreferencesViewModel
import ch.hslu.mobpro.packing_list.viewmodels.ItemViewModel
import ch.hslu.mobpro.packing_list.viewmodels.ItemViewModelFactory

/**
 * Display all items of a Single Packing List
 */
class PacklistFragment : Fragment() {

    private val args: PacklistFragmentArgs by navArgs()

    private val itemViewModel: ItemViewModel by viewModels {
        ItemViewModelFactory((requireActivity().application as PacklistApplication).repository)
    }

    private val sharedPreferencesViewModel: SharedPreferencesViewModel by activityViewModels()


    private var _binding: FragmentPacklistBinding? = null
    private val binding get() = _binding!!

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

        // sharedPreferencesViewModel.setDefaultPreferences()

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
                    val itemToDelete =
                        adapter.getItemAt(viewHolder.absoluteAdapterPosition)?.itemContentID
                    if (itemToDelete != null) {
                        Log.d(TAG, "deleting item $itemToDelete")
                        itemViewModel.delete(itemToDelete)
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

        if (action != null) {
            findNavController().navigate(action)
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
        itemViewModel.getCurrentEditingPackList().observe(viewLifecycleOwner) { matchingTitlePacklist ->
            Log.v(TAG, "successfully retrieved matchingTitlePacklist")
            currentPackListTitle = matchingTitlePacklist[0].title
        }


        // Set Title // Get Items
        currentPackListTitle?.let { title ->
            binding.textViewPacklistTitle.text = currentPackListTitle


            itemViewModel.getItems(title).observe(viewLifecycleOwner) { items ->
                Log.v(TAG, "itemViewModel.getItems(it).observe")
                if (items.isNotEmpty()) { // List can have size 0 if no items have been created yet
                    val packlistWithItems = items[0]
                    val itemList = packlistWithItems.items // This is a List of the actual items of packlist
                    itemList.let { adapter.submitList(it) } // this generates the Items
                } else {
                    adapter.submitList(emptyList())
                }
            }
        }

        itemViewModel._navigateBackToItemOverview.observe(viewLifecycleOwner) {
            navigateBack()
        }
        /** Columns can be changed dynamically in prefs */
        sharedPreferencesViewModel.getPreferencesSummary().observe(viewLifecycleOwner) { cols ->
            Log.d(TAG, "getPreferencesSummary $cols")
            binding.itemRecyclerView.layoutManager = GridLayoutManager(requireContext(), cols)
        }

    }

    private fun navigateBack() {
        Log.d(TAG, "navigateBack")
        val action = currentPackListTitle?.let {
            CreateItemFragmentDirections.actionCreateItemFragmentToPacklistFragment(it)
        }
        if (action != null) {
            findNavController().navigate(action)
        } else {
            findNavController().navigate(R.id.action_CreateItemFragment_to_PacklistFragment)
        }
    }

    override fun onResume() {
        super.onResume()
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