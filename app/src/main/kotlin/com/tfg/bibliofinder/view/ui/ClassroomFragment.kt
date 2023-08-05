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
import com.tfg.bibliofinder.databinding.FragmentClassroomBinding
import com.tfg.bibliofinder.model.entities.Classroom
import com.tfg.bibliofinder.model.entities.ClassroomMockDataProvider
import com.tfg.bibliofinder.view.adapters.ClassroomAdapter
import com.tfg.bibliofinder.viewmodel.ClassroomViewModel

class ClassroomFragment : Fragment() {

    private var _binding: FragmentClassroomBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ClassroomAdapter
    private val classrooms = mutableListOf<Classroom>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val classroomViewModel = ViewModelProvider(this)[ClassroomViewModel::class.java]

        _binding = FragmentClassroomBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager

        adapter = ClassroomAdapter(classrooms) { classroom ->
            navigateToWorkstations(classroom)
        }
        recyclerView.adapter = adapter

        val libraryId = arguments?.getLong("libraryId", -1L)
        if (libraryId != null && libraryId != -1L) {
            initializeData(libraryId)
        }

        return root
    }

    private fun navigateToWorkstations(classroom: Classroom) {
        val bundle = Bundle().apply {
            putLong("classroomId", classroom.classroomId)
            // Add other relevant data if needed.
        }

        findNavController().navigate(R.id.action_nav_classroom_to_nav_workstation, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeData(libraryId: Long) {
        classrooms.addAll(
            ClassroomMockDataProvider.getMockClassrooms().filter { it.libraryId == libraryId })
    }
}