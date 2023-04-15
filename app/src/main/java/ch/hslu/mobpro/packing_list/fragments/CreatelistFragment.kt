package ch.hslu.mobpro.packing_list.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ch.hslu.mobpro.packing_list.PacklistApplication
import ch.hslu.mobpro.packing_list.databinding.FragmentCreatelistBinding
import ch.hslu.mobpro.packing_list.room.Packlist
import ch.hslu.mobpro.packing_list.utils.CommonUtils.Companion.getRandomColor
import ch.hslu.mobpro.packing_list.viewmodels.PacklistViewModel
import ch.hslu.mobpro.packing_list.viewmodels.PacklistViewModelFactory

/**
 *  FloatingActionButton -> creating a new list.
 *  OR
 *  Edit the current top-level item.
 *
 */
class CreatelistFragment : Fragment() {

    private val packlistViewModel: PacklistViewModel by viewModels {
        PacklistViewModelFactory((requireActivity().application as PacklistApplication).repository)
    }

    private var _binding: FragmentCreatelistBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCreatelistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModels()
        binding.mainButtonSubmitList.setOnClickListener { submitListOnClick() }
    }



    private fun submitListOnClick() {
        val packlist = createNewPacklistObject()
        packlistViewModel.insertNewPacklist(packlist)
    }

    private fun createNewPacklistObject(): Packlist {
        val title = binding.mainEditTextName.text.toString()
        return Packlist(title, "", "foo", getRandomColor())
    }




    private fun observeViewModels() {
        packlistViewModel._navigateBacktoMenu.observe(viewLifecycleOwner) {
            navigateBack()
        }

        packlistViewModel.allPacklists.observe(viewLifecycleOwner) { allPacklists ->
            val index = allPacklists.size + 1
            val defaultTitle =
                """
                $index
                """.trimIndent()
            binding.mainEditTextName.setText(defaultTitle)

        }
    }

    private fun navigateBack() {
        findNavController().navigate(ch.hslu.mobpro.packing_list.R.id.action_CreateListFragment_To_MenuFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "CreateListFragment"
    }

}