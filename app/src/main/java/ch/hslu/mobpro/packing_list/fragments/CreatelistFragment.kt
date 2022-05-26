package ch.hslu.mobpro.packing_list.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ch.hslu.mobpro.packing_list.PacklistApplication
import ch.hslu.mobpro.packing_list.viewmodels.PacklistViewModel
import ch.hslu.mobpro.packing_list.viewmodels.PacklistViewModelFactory
import ch.hslu.mobpro.packing_list.R
import ch.hslu.mobpro.packing_list.databinding.FragmentCreatelistBinding
import ch.hslu.mobpro.packing_list.room.Packlist
import java.util.*

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
        _binding = FragmentCreatelistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModels()
        binding.mainButtonSubmitList.setOnClickListener { submitListOnClick() }
        binding.mainButtonStartDatePicker.setOnClickListener{ setupDatePicker()}

        binding.spinnerView.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
            Log.d(TAG, "spinnerclicked")
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        }
        binding.spinnerView.apply {
            setOnSpinnerItemSelectedListener<String> { _, _, index, _ ->
                when (index) {
                    0 -> setBackgroundColor(getContextColor(R.color.colorPrimary))
                    1 -> setBackgroundColor(getContextColor(R.color.md_orange_200))
                    2 -> setBackgroundColor(getContextColor(R.color.md_yellow_200))
                    3 -> setBackgroundColor(getContextColor(R.color.md_green_200))
                    4 -> setBackgroundColor(getContextColor(R.color.md_blue_200))
                    5 -> setBackgroundColor(getContextColor(R.color.md_purple_200))
                }
            }
        }
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


        val dpd = DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener { datePicker, year, month, day -> setDate(year, month, day) }, year, month, day)
        dpd.show()


    }

    @SuppressLint("SetTextI18n")
    private fun setDate( year: Int, month: Int, day: Int) {
        binding.datePickerTextView.text = "$day/$month/$year"
    }


    private fun createNewPacklistObject(): Packlist {
        val packListLocation = binding.mainEditTextLocation.text.toString()
        val packListTitle = binding.mainEditTextName.text.toString()
        val packListDate = binding.datePickerTextView.text.toString()
        return Packlist(packListTitle, packListLocation, packListDate)
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

    private fun getContextColor(@ColorRes resource: Int): Int {
        return ContextCompat.getColor(requireContext(), resource)
    }


    companion object {
        private const val TAG = "CreateListFragment"
    }

}