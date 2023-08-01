package com.tfg.bibliofinder.ui.library

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
import com.tfg.bibliofinder.entities.Library

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LibraryAdapter
    private val libraries = mutableListOf<Library>()

    // This property is only valid between onCreateView and onDestroyView.
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

        // If libraries list is empty, initialize data (prevents duplicating data on configuration changes)
        if (libraries.isEmpty()) {
            initializeData()
        }

        return root
    }

    private fun navigateToClassrooms(library: Library) {
        // You can pass the library ID or any relevant information to the ClassroomFragment here.
        val bundle = Bundle().apply {
            putLong("libraryId", library.libraryId)
            // Add other relevant data if needed.
        }

        // Navigate to ClassroomFragment with the relevant information.
        findNavController().navigate(R.id.action_nav_library_to_nav_classroom, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeData() {
        // TODO: Replace with DB libraries list.
        libraries.addAll(createMockLibraryData())
        adapter.notifyDataSetChanged()
    }

    // Mock data for LibraryFragment
    private fun createMockLibraryData(): List<Library> {
        val libraries = mutableListOf<Library>()
        libraries.add(
            Library(
                1L,
                "Biblioteca Xoana Capdevielle",
                "9:00 AM - 7:00 PM",
                654,
                "Campus de Elviña",
                "555-1234",
                "info@bibliotecaxoana.com",
                true,
                "Pública",
                "Universidad de A Coruña"
            )
        )
        libraries.add(
            Library(
                2L,
                "Centro Universitario de Riazor",
                "8:00 AM - 10:00 PM",
                630,
                "Campus de Riazor",
                "555-5678",
                "info@centrouriazor.com",
                true,
                "Académica",
                "Universidad de A Coruña"
            )
        )
        return libraries
    }
}
