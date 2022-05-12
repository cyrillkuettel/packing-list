package ch.hslu.mobpro.packing_list.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ch.hslu.mobpro.packing_list.PacklistApplication
import ch.hslu.mobpro.packing_list.PacklistViewModel
import ch.hslu.mobpro.packing_list.PacklistViewModelFactory
import ch.hslu.mobpro.packing_list.R
import ch.hslu.mobpro.packing_list.database.Packlist
import ch.hslu.mobpro.packing_list.databinding.FragmentCreatelistBinding

/**
 *  This fragment represents the screen for creating a new packing list.
 *
 */
class CreatelistFragment : Fragment() {

    private val packlistViewModel: PacklistViewModel by viewModels {
        PacklistViewModelFactory((requireActivity().application as PacklistApplication).repository)
    }

    private var _binding: FragmentCreatelistBinding? = null
    private val binding get() = _binding!!

    private var currentPackList: Packlist? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCreatelistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModels()
        binding.mainButtonSubmitList.setOnClickListener { submitListOnClick() }
    }


    private fun observeViewModels() {
        packlistViewModel.checkCurrentPackList?.observe(viewLifecycleOwner,
            Observer { doesAlreadyExist ->

                if (doesAlreadyExist) {
                    Log.v(TAG, "doesAlreadyExists")
                    Toast.makeText(
                        requireActivity(),
                        "Exists already",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.v(TAG, "naviateback")
                    currentPackList?.let { packlistViewModel.insert(it) }
                    findNavController().navigate(R.id.action_CreateListFragment_To_MenuFragment)
                }
            })
    }

    // TODO : input validation
    private fun submitListOnClick() {

        currentPackList = populatePacklistObject()
        currentPackList?.let { packlistViewModel.insert(it) }
        findNavController().navigate(R.id.action_CreateListFragment_To_MenuFragment)
        // packlistViewModel.startExistenceCheck(currentPackList!!)

    }

    private fun populatePacklistObject(): Packlist {
        // TODO : more attributes like date, location etc
        val packListTitle = binding.mainEditTextName.text.toString()
        return Packlist(packListTitle)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "CreateListFragment"
    }

}