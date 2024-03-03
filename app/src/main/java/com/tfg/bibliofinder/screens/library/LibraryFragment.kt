package com.tfg.bibliofinder.screens.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.FragmentLibraryBinding
import com.tfg.bibliofinder.entities.Library
import com.tfg.bibliofinder.util.ItemClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryFragment : Fragment(), ItemClickListener<Library> {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    private val libraries = mutableListOf<Library>()

    private val adapter: LibraryAdapter by lazy { LibraryAdapter(libraries, this) }
    private val viewModel: LibraryViewModel by viewModel()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@LibraryFragment.adapter
        }

        initializeLibrarySortingExposedDropdown()

        viewModel.allLibraries.observe(viewLifecycleOwner) { libraries ->
            this.libraries.clear()
            this.libraries.addAll(libraries)
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    private fun initializeLibrarySortingExposedDropdown() {
        val sortingOptions = resources.getStringArray(R.array.library_sorting_options)

        val exposedDropdownAdapter =
            ArrayAdapter(requireContext(), R.layout.list_item, sortingOptions)

        val exposedDropdownMenu = binding.exposedDropdownMenu
        exposedDropdownMenu.setAdapter(exposedDropdownAdapter)
        exposedDropdownMenu.setText(sortingOptions[0], false)

        val sortingFunctions =
            mapOf(getString(R.string.name_ascending) to { libraries.sortBy { it.name } },
                getString(R.string.name_descending) to { libraries.sortByDescending { it.name } },
                getString(R.string.opening_time) to { libraries.sortBy { it.openingTime } },
                getString(R.string.closing_time) to { libraries.sortBy { it.closingTime } },
                getString(R.string.free_spaces) to { libraries.sortByDescending { it.freeSpaces } },
                getString(R.string.adapted_for_disabilities) to { libraries.sortByDescending { it.isAdapted } },
                getString(R.string.by_institution) to { libraries.sortBy { it.institution } })

        exposedDropdownMenu.setOnItemClickListener { _, _, position, _ ->
            val selectedOption = sortingOptions[position]
            sortingFunctions[selectedOption]?.invoke()
            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        initializeLibrarySortingExposedDropdown()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(item: Library) {
        val bundle = Bundle().apply { putLong("libraryId", item.libraryId) }
        findNavController().navigate(R.id.action_nav_library_to_nav_classroom, bundle)
    }

    override fun onInfoButtonClick(item: Library) {
        val bottomSheetFragment = LibraryBottomSheetFragment(item)
        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
    }

    override fun onBookButtonClick(item: Library) {
        TODO("Not yet implemented")
    }
}