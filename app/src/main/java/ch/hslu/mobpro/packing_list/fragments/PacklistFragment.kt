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
        observeViewModels()

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


    private fun observeViewModels() {
        Log.v(TAG, "observeViewModels")
        itemViewModel.getCurrentEditingPackList().observe(viewLifecycleOwner) { matchingTitlePacklist ->
            Log.v(TAG, "successfully retrieved matchingTitlePacklist")
            currentPackListTitle = matchingTitlePacklist[0].title
        }

        currentPackListTitle?.let {
            itemViewModel.getItems(it).observe(viewLifecycleOwner) { items ->
                Log.v(TAG, "itemViewModel.getItems(it).observe")
                if (items.isNotEmpty()) {
                    val displayItems = items[0].items
                    addItemsToView(displayItems)
                }
            }
        }
    }

    private fun addItemsToView(items: List<Item>) {
        val linearlayout = binding.placeHolderContent
        for (displayItem in items) {
            val edittext = TextView(requireActivity())
            edittext.text = displayItem.content
            edittext.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            linearlayout.addView(edittext)

            Log.v(TAG, items.toString())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "PacklistFragment"
    }

}