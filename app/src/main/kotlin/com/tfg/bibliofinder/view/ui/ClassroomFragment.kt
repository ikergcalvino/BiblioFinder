package com.tfg.bibliofinder.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tfg.bibliofinder.databinding.FragmentClassroomBinding
import com.tfg.bibliofinder.model.Classroom
import com.tfg.bibliofinder.view.adapters.ClassroomAdapter
import com.tfg.bibliofinder.viewmodel.ClassroomViewModel

class ClassroomFragment : Fragment() {

    private var _binding: FragmentClassroomBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ClassroomAdapter
    private val classrooms = mutableListOf<Classroom>()

    // This property is only valid between onCreateView and onDestroyView.
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
            // Handle item click here if needed
        }
        recyclerView.adapter = adapter

        // Get the library ID from the arguments passed by LibraryFragment
        val libraryId = arguments?.getLong("libraryId", -1L)
        if (libraryId != null && libraryId != -1L) {
            initializeData(libraryId)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeData(libraryId: Long) {
        // TODO: Replace with DB classrooms list based on the libraryId.
        val classroomsForLibrary = createMockClassroomData().filter { it.libraryId == libraryId }
        classrooms.clear()
        classrooms.addAll(classroomsForLibrary)
        adapter.notifyDataSetChanged()
    }

    // Mock data for ClassroomFragment
    private fun createMockClassroomData(): List<Classroom> {
        val classrooms = mutableListOf<Classroom>()
        classrooms.add(
            Classroom(
                1L, "Sala de Estudio 1", 50, "Área de Estudio en Grupo", 1L
            )
        )
        classrooms.add(
            Classroom(
                2L, "Sala de Estudio 2", 40, "Área de Trabajo en Silencio", 1L
            )
        )
        classrooms.add(
            Classroom(
                3L, "Sala de Reuniones 1", 30, "Sala de Colaboración", 2L
            )
        )
        classrooms.add(
            Classroom(
                4L, "Sala de Reuniones 2", 20, "Sala de Estudio Individual", 2L
            )
        )
        classrooms.add(
            Classroom(
                5L, "Sala de Conferencias", 25, "Sala de Presentaciones", 2L
            )
        )
        return classrooms
    }
}