package ch.hslu.mobpro.packing_list.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ch.hslu.mobpro.packing_list.PacklistApplication
import ch.hslu.mobpro.packing_list.databinding.FragmentCreatelistBinding
import ch.hslu.mobpro.packing_list.room.Packlist
import ch.hslu.mobpro.packing_list.viewmodels.PacklistViewModel
import ch.hslu.mobpro.packing_list.viewmodels.PacklistViewModelFactory
import java.util.*


/**
 *  This fragment represents the screen for creating a new packing list.
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
        binding.mainButtonStartDatePicker.setOnClickListener{ setupDatePicker() }


        binding.spinnerView.apply {
            setOnSpinnerItemSelectedListener<String> { _, _, index, _ ->
                val rainbow = context.resources.getIntArray(
                    ch.hslu.mobpro.packing_list.R.array.packlist_background_colors)
                if (index >= rainbow.size || index < 0) {
                    Log.e(TAG, "index does not exit")
                } else {
                    val selectedBackgroundColor = rainbow[index]
                    setBackgroundColor(selectedBackgroundColor)
                }
            }
        }
    }

    private fun submitListOnClick() {
        // TODO : input validation
        val packlist = createNewPacklistObject()
        packlistViewModel.insertNewPacklist(packlist)
    }

    private fun createNewPacklistObject(): Packlist {
        val location = binding.mainEditTextLocation.text.toString()
        val title = binding.mainEditTextName.text.toString()
        val date = binding.datePickerTextView.text.toString()
        val color = (binding.spinnerView.background as ColorDrawable).color
        return Packlist(title, location, date, color)
    }

    @SuppressLint("SetTextI18n")
    private fun setupDatePicker(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(requireContext(),
            { datePicker, year, month, day -> setDate(year, month, day) }, year, month, day)
        dpd.show()
    }

    @SuppressLint("SetTextI18n")
    private fun setDate( year: Int, month: Int, day: Int) {
        binding.datePickerTextView.text = "$day/$month/$year"
    }



    private fun observeViewModels() {
        packlistViewModel._navigateBacktoMenu.observe(viewLifecycleOwner) {
            navigateBack()
        }

        packlistViewModel.allPacklists.observe(viewLifecycleOwner) { allPacklists ->
            val index = allPacklists.size + 1
            val defaultName =
                """
                packlist_$index
                """.trimIndent()
            binding.mainEditTextName.setText(defaultName)

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