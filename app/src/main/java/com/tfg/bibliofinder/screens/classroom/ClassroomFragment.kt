package com.tfg.bibliofinder.screens.classroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.FragmentClassroomBinding
import com.tfg.bibliofinder.entities.Classroom
import com.tfg.bibliofinder.util.ItemClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class ClassroomFragment : Fragment(), ItemClickListener<Classroom> {

    private val classrooms = mutableListOf<Classroom>()
    private var _binding: FragmentClassroomBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ClassroomAdapter
    private lateinit var recyclerView: RecyclerView
    private val viewModel: ClassroomViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassroomBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ClassroomAdapter(classrooms, this)
        recyclerView.adapter = adapter

        val libraryId = arguments?.getLong("libraryId", 0L)
        if (libraryId != null && libraryId != 0L) {
            viewModel.getClassroomsByLibrary(libraryId).observe(viewLifecycleOwner) { classrooms ->
                this.classrooms.clear()
                this.classrooms.addAll(classrooms)
                adapter.notifyDataSetChanged()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(item: Classroom) {
        val bundle = Bundle().apply { putLong("classroomId", item.classroomId) }
        findNavController().navigate(R.id.action_nav_classroom_to_nav_workstation, bundle)
    }

    override fun onInfoButtonClick(item: Classroom) {
        TODO("Not yet implemented")
    }

    override fun onBookButtonClick(item: Classroom) {
        TODO("Not yet implemented")
    }
}