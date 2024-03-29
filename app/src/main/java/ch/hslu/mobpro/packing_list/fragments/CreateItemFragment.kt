package ch.hslu.mobpro.packing_list.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ch.hslu.mobpro.packing_list.PacklistApplication
import ch.hslu.mobpro.packing_list.R
import ch.hslu.mobpro.packing_list.databinding.FragmentCreateItemBinding
import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.utils.CommonUtils.Companion.getRandomColor
import ch.hslu.mobpro.packing_list.viewmodels.ItemViewModel
import ch.hslu.mobpro.packing_list.viewmodels.ItemViewModelFactory
import java.util.*


class CreateItemFragment : Fragment() {

    private val args: CreateItemFragmentArgs by navArgs()

    private val itemViewModel: ItemViewModel by viewModels {
        ItemViewModelFactory((requireActivity().application as PacklistApplication).repository)
    }

    private var _binding: FragmentCreateItemBinding? = null
    private val binding get() = _binding!!

    private var currentPackListUUID: UUID? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCreateItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainButtonAddItem.setOnClickListener { submitItemOnclick() }
        val s = args.uuid
        currentPackListUUID = UUID.fromString(s)
        Log.v(TAG, "getting arguments,currentPackListTitle is $currentPackListUUID")


        observeViewModels()

    }


    private fun observeViewModels() {
        itemViewModel._navigateBackToItemOverview.observe(viewLifecycleOwner) {
            navigateBack()
        }
    }

    private fun navigateBack() {
        // XXX Pass the arguments to fragment
        val action: NavDirections? = currentPackListUUID?.let {
            CreateItemFragmentDirections.actionCreateItemFragmentToPacklistFragment(it.toString())
        }
        if (action != null) {
            findNavController().navigate(action)
        } else {
            findNavController().navigate(R.id.action_CreateItemFragment_to_PacklistFragment)
        }
    }


    private fun submitItemOnclick() {
        createItem()?.apply {
            itemViewModel.insertNewItem(this)
        } ?: Log.e(TAG, "created item is null")
    }

    private fun createItem(): Item? {
        val content = binding.mainEditTextItemName.text.toString()

        /** Items reference the parent by the title [currentPackListUUID] */
        return currentPackListUUID?.let { Item(it, content,  getRandomColor()) }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "CreateItemFragment"
    }
}