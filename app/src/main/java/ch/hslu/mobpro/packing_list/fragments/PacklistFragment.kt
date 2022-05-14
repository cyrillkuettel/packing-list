package ch.hslu.mobpro.packing_list.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ch.hslu.mobpro.packing_list.*
import ch.hslu.mobpro.packing_list.databinding.FragmentPacklistBinding
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
        Log.v(TAG, "getting arguments, args.title is $title")
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

          //  val packlist = matchingTitlePacklist[0]
            // val uniquePackListId = pac.id
        }
/*
        itemViewModel.getCurrentItems(currentPackListTitle).observe(viewLifecycleOwner) { matchingTitlePacklist ->
            Log.v(TAG, "successfully retrieved matchingTitlePacklist")
            currentPackListTitle = matchingTitlePacklist[0].title

            //  val packlist = matchingTitlePacklist[0]
            // val uniquePackListId = pac.id
        }
*/


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "PacklistFragment"
    }

}