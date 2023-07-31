package com.tfg.bibliofinder.ui.classroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tfg.bibliofinder.databinding.FragmentClassroomBinding
import com.tfg.bibliofinder.entities.Classroom

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

        initializeData()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeData() {
        // TODO: Replace with DB classrooms list.
        classrooms.addAll(createMockClassroomData())
        adapter.notifyDataSetChanged()
    }

    // Mock data for ClassroomFragment
    private fun createMockClassroomData(): List<Classroom> {
        val classrooms = mutableListOf<Classroom>()
        classrooms.add(
            Classroom(
                1, "Sala de Estudio 1", 50, "Área de Estudio en Grupo", 1
            )
        )
        classrooms.add(
            Classroom(
                2, "Sala de Estudio 2", 40, "Área de Trabajo en Silencio", 1
            )
        )
        classrooms.add(
            Classroom(
                3, "Sala de Reuniones 1", 30, "Sala de Colaboración", 2
            )
        )
        classrooms.add(
            Classroom(
                4, "Sala de Reuniones 2", 20, "Sala de Estudio Individual", 2
            )
        )
        classrooms.add(
            Classroom(
                5, "Sala de Conferencias", 25, "Sala de Presentaciones", 2
            )
        )
        return classrooms
    }
}
