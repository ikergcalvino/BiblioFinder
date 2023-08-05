package com.tfg.bibliofinder.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.FragmentLibraryBinding
import com.tfg.bibliofinder.model.entities.Library
import com.tfg.bibliofinder.model.entities.LibraryMockDataProvider
import com.tfg.bibliofinder.view.adapters.LibraryAdapter
import com.tfg.bibliofinder.viewmodel.LibraryViewModel

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LibraryAdapter
    private val libraries = mutableListOf<Library>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val libraryViewModel = ViewModelProvider(this)[LibraryViewModel::class.java]

        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager

        adapter = LibraryAdapter(libraries) { library ->
            navigateToClassrooms(library)
        }
        recyclerView.adapter = adapter

        if (libraries.isEmpty()) {
            initializeData()
        }

        return root
    }

    private fun navigateToClassrooms(library: Library) {
        val bundle = Bundle().apply {
            putLong("libraryId", library.libraryId)
            // Add other relevant data if needed.
        }

        findNavController().navigate(R.id.action_nav_library_to_nav_classroom, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeData() {
        libraries.addAll(LibraryMockDataProvider.getMockLibraries())
    }
}