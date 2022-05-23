package ch.hslu.mobpro.packing_list.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ch.hslu.mobpro.packing_list.PacklistApplication
import ch.hslu.mobpro.packing_list.viewmodels.PacklistViewModel
import ch.hslu.mobpro.packing_list.viewmodels.PacklistViewModelFactory
import ch.hslu.mobpro.packing_list.R
import ch.hslu.mobpro.packing_list.databinding.FragmentCreatelistBinding
import ch.hslu.mobpro.packing_list.room.Packlist
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

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
        binding.mainButtonStartDatePicker.setOnClickListener{ setupDatePicker()}
    }

    private fun submitListOnClick() {
        // TODO : input validation
        val packlist = createNewPacklistObject()
        packlistViewModel.insertNewPacklist(packlist)
    }

    @SuppressLint("SetTextI18n")
    private fun setupDatePicker(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), null, year, month, day)
        dpd.show()
        binding.textView3.text = "$day/$month/$year"
    }


    private fun createNewPacklistObject(): Packlist {
        // TODO : insert attributes like date, location etc
        val packListLocation = binding.mainEditTextLocation.text.toString()
        val packListTitle = binding.mainEditTextName.text.toString()
        return Packlist(packListTitle, packListLocation)
    }


    private fun observeViewModels() {
        packlistViewModel._navigateBacktoMenu.observe(viewLifecycleOwner) {
            navigateBack()
        }
    }

    private fun navigateBack() {
        findNavController().navigate(R.id.action_CreateListFragment_To_MenuFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val TAG = "CreateListFragment"
    }

}