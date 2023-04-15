package ch.hslu.mobpro.packing_list.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ch.hslu.mobpro.packing_list.*
import ch.hslu.mobpro.packing_list.databinding.FragmentCreateItemBinding
import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.utils.CommonUtils.Companion.getRandomColor
import ch.hslu.mobpro.packing_list.viewmodels.ItemViewModel
import ch.hslu.mobpro.packing_list.viewmodels.ItemViewModelFactory


class CreateItemFragment : Fragment() {

    private val args: CreateItemFragmentArgs by navArgs()

    private val itemViewModel: ItemViewModel by viewModels {
        ItemViewModelFactory((requireActivity().application as PacklistApplication).repository)
    }

    private var _binding: FragmentCreateItemBinding? = null
    private val binding get() = _binding!!

    private var currentPackListTitle: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCreateItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("LogConditional")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainButtonAddItem.setOnClickListener { submitItemOnclick() }
        currentPackListTitle = args.titleFromClicked
        Log.v(TAG, "getting arguments,currentPackListTitle is $currentPackListTitle")

        binding.mainEditTextItemName.requestFocus()
        binding.mainEditTextItemName.showKeyboard()

        observeViewModels()

    }

    /** fancy kotlin extension function ( ͡° ͜ʖ ͡°) */
    private fun View.showKeyboard() {
        this.requestFocus()
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun observeViewModels() {
        itemViewModel._navigateBackToItemOverview.observe(viewLifecycleOwner) {
            navigateBack()
        }
    }

    private fun navigateBack() {
        val action = currentPackListTitle?.let {
            CreateItemFragmentDirections.actionCreateItemFragmentToPacklistFragment(it)
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
        val status = false
        /** Items reference the parent by the title [currentPackListTitle] */
        return currentPackListTitle?.let { Item(it, content, status, getRandomColor()) }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "CreateItemFragment"
    }
}