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

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val libraryViewModel = ViewModelProvider(this).get(LibraryViewModel::class.java)

        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager
        initializeData()
        adapter = LibraryAdapter(libraries) { library ->
            // Handle item click here if needed
        }
        recyclerView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeData() {
        // TODO: Replace with your logic to populate 'libraries' list with data
    }
}