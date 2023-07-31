package com.tfg.bibliofinder.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
            // Handle item click here if needed
        }
        recyclerView.adapter = adapter

        initializeData()

        return root
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
                1,
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
                2,
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
