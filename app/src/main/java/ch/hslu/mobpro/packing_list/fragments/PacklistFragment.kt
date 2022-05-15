package ch.hslu.mobpro.packing_list.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import ch.hslu.mobpro.packing_list.*
import ch.hslu.mobpro.packing_list.databinding.FragmentPacklistBinding
import ch.hslu.mobpro.packing_list.room.Item
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

    private var _binding: FragmentPacklistBinding? = null
    private val binding get() = _binding!!

    private var currentPackListTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        currentPackListTitle = title // not optimal, storing data in fragment, but what else
        val adapter = setupRecyclerView()

        observeViewModels(adapter)

        binding.fabCreateNewNote.setOnClickListener {
            navigateToCreateItemFragment()
        }


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
        val adapter = ItemAdapter(itemViewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), NUMBER_OF_COLUMNS)
        return adapter
    }


    private fun observeViewModels(adapter: ItemAdapter) {
        Log.v(TAG, "observeViewModels")
        itemViewModel.getCurrentEditingPackList().observe(viewLifecycleOwner) { matchingTitlePacklist ->
            Log.v(TAG, "successfully retrieved matchingTitlePacklist")
            currentPackListTitle = matchingTitlePacklist[0].title
        }

        currentPackListTitle?.let { title ->
            binding.textViewPacklistTitle.text = currentPackListTitle
            itemViewModel.getItems(title).observe(viewLifecycleOwner) { items ->
                Log.v(TAG, "itemViewModel.getItems(it).observe")
                if (items.isNotEmpty()) { // List can have size 0 if no items have been created yet
                    val packlistWithItems = items[0]
                    val itemList = packlistWithItems.items
                    itemList.let { adapter.submitList(it) }
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val NUMBER_OF_COLUMNS = 3
        private const val TAG = "PacklistFragment"
    }

}