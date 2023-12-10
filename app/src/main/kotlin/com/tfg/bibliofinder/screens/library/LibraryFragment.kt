package com.tfg.bibliofinder.screens.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.FragmentLibraryBinding
import com.tfg.bibliofinder.entities.Library
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryFragment : Fragment() {

    private val libraries = mutableListOf<Library>()
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: LibraryAdapter
    private lateinit var recyclerView: RecyclerView
    private val viewModel: LibraryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = LibraryAdapter(libraries) { library ->
            val bundle = Bundle().apply { putLong("libraryId", library.libraryId) }

            findNavController().navigate(R.id.action_nav_library_to_nav_classroom, bundle)
        }
        recyclerView.adapter = adapter

        initializeLibrarySortingSpinner()

        viewModel.allLibraries.observe(viewLifecycleOwner) { libraries ->
            this.libraries.clear()
            this.libraries.addAll(libraries)
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    private fun initializeLibrarySortingSpinner() {
        val sortingOptions = resources.getStringArray(R.array.library_sorting_options)
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortingOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = spinnerAdapter

        val sortingFunctions =
            mapOf(getString(R.string.libraries_name_ascending) to { libraries.sortBy { it.name } },
                getString(R.string.libraries_name_descending) to { libraries.sortByDescending { it.name } },
                getString(R.string.libraries_opening_time) to { libraries.sortBy { it.openingTime } },
                getString(R.string.libraries_closing_time) to { libraries.sortBy { it.closingTime } },
                getString(R.string.libraries_free_spaces) to { libraries.sortByDescending { it.freeSpaces } },
                getString(R.string.libraries_adapted_for_disabilities) to { libraries.sortByDescending { it.isAdapted } },
                getString(R.string.libraries_by_institution) to { libraries.sortBy { it.institution } })

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedOption = sortingOptions[position]
                sortingFunctions[selectedOption]?.invoke()
                adapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                libraries.sortBy { it.libraryId }
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}