package ch.hslu.mobpro.packing_list

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.hslu.mobpro.packing_list.databinding.FragmentItemBinding
import ch.hslu.mobpro.packing_list.databinding.FragmentSecondBinding

/**
 * A fragment representing a list of packing lists.
 * This is the first fragment that is shown.
 */
class ItemFragment : Fragment() {

    private var _binding: FragmentItemBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val packlistViewModel: PacklistViewModel by viewModels {
        PacklistViewModelFactory((requireActivity().application as PacklistApplication).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentItemBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = PackListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Add an observer on the LiveData returned by getAlphabetizedPacklist.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        packlistViewModel.allPacklists.observe(viewLifecycleOwner) { packlist ->
            // Update the cached copy of the words in the adapter.
            packlist.let { adapter.submitList(it) }
        }
    }

    companion object {
        private const val TAG = "ItemFragment"
    }
}