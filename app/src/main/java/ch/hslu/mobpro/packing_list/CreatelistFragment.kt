package ch.hslu.mobpro.packing_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ch.hslu.mobpro.packing_list.databinding.FragmentCreatelistBinding

/**
 *  This fragment represents the screen for creating a new packing list.
 *
 */
class CreatelistFragment : Fragment() {

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
        binding.mainButtonSubmitList.setOnClickListener { submitListOnClick() }
    }

    private fun submitListOnClick() {
        findNavController().navigate(R.id.action_CreateListFragment_To_ItemFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}