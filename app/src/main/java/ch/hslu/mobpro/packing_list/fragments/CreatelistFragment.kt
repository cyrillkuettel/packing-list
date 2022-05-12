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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ch.hslu.mobpro.packing_list.PacklistApplication
import ch.hslu.mobpro.packing_list.PacklistViewModel
import ch.hslu.mobpro.packing_list.PacklistViewModelFactory
import ch.hslu.mobpro.packing_list.R
import ch.hslu.mobpro.packing_list.database.Packlist
import ch.hslu.mobpro.packing_list.databinding.FragmentCreatelistBinding
import kotlinx.coroutines.launch

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

    /** The new packlist which is being create d*/
    private var currentPacklist: Packlist? = null

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
        // observeViewModels()
        binding.mainButtonSubmitList.setOnClickListener { submitListOnClick() }
    }





    private fun submitListOnClick() {
        currentPacklist = createNewPacklistObject()
        navigateBack()  // TODO : input validation
    }


    private fun createNewPacklistObject(): Packlist {
        // TODO : insert attributes like date, location etc
        val packListTitle = binding.mainEditTextName.text.toString()
        return Packlist(packListTitle)
    }

    private fun navigateBack() {
        findNavController().navigate(R.id.action_CreateListFragment_To_MenuFragment)
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
                    currentPacklist?.let {
                        lifecycleScope.launch{
                            val id =  packlistViewModel.insertPacklist(it)
                        }

                    }
                    findNavController().navigate(R.id.action_CreateListFragment_To_MenuFragment)
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val TAG = "CreateListFragment"
    }

}