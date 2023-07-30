package com.tfg.bibliofinder.ui.classroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tfg.bibliofinder.databinding.FragmentClassroomBinding

class ClassroomFragment : Fragment() {

    private var _binding: FragmentClassroomBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val classroomViewModel =
            ViewModelProvider(this).get(ClassroomViewModel::class.java)

        _binding = FragmentClassroomBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textClassroom
        classroomViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}