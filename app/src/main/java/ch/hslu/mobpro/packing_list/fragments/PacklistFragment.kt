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
import ch.hslu.mobpro.packing_list.PacklistApplication
import ch.hslu.mobpro.packing_list.PacklistViewModel
import ch.hslu.mobpro.packing_list.PacklistViewModelFactory
import ch.hslu.mobpro.packing_list.R
import ch.hslu.mobpro.packing_list.databinding.FragmentPacklistBinding

/**
 * This fragment can later be used to display the contents of a single packing list
 * Right now it doesn't do much.
 */
class PacklistFragment : Fragment() {

    private val args: PacklistFragmentArgs by navArgs()

    private val packlistViewModel: PacklistViewModel by viewModels {
        PacklistViewModelFactory((requireActivity().application as PacklistApplication).repository)
    }



    private var _binding: FragmentPacklistBinding? = null
    private val binding get() = _binding!!

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
        val title = args.title
        packlistViewModel.setCurrentEditingPackListTitle(title)
        observeViewModels()
    }


    private fun observeViewModels() {
        Log.v(TAG, "observeViewModels")
        packlistViewModel.getCurrentEditingPackList().observe(viewLifecycleOwner) { matchingTitlePacklists ->
            Log.v(TAG, "SUCCESSFULLL RETRIVED ELEMENT")
            val pac = matchingTitlePacklists[0]
            Log.v(TAG, pac.toString())

        }
        /*
        packlistViewModel.getClickedPacklist().observe(viewLifecycleOwner) { clickedPacklist ->
            if (clickedPacklist != null) {
                Log.v(TAG, clickedPacklist.title)
                binding.textViewTest.text = clickedPacklist.title
            } else {
                Log.v(TAG, "clickedpacklist null")
            }
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