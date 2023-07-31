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

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val classroomViewModel =
            ViewModelProvider(this)[ClassroomViewModel::class.java]

        _binding = FragmentClassroomBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager
        initializeData()
        adapter = ClassroomAdapter(classrooms) { classroom ->
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
        // TODO: Replace with DB classrooms list.
        classrooms.addAll(createMockClassroomData())
        adapter.notifyDataSetChanged()
    }

    // Mock data for ClassroomFragment
    private fun createMockClassroomData(): List<Classroom> {
        val classrooms = mutableListOf<Classroom>()
        classrooms.add(
            Classroom(
                1,
                "Classroom 101",
                50,
                "Lecture Hall"
            )
        )
        classrooms.add(
            Classroom(
                2,
                "Classroom 202",
                40,
                "Lab"
            )
        )
        return classrooms
    }
}
