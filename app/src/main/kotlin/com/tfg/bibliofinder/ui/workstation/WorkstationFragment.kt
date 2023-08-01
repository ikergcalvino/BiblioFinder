package com.tfg.bibliofinder.ui.workstation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tfg.bibliofinder.databinding.FragmentWorkstationBinding

class WorkstationFragment : Fragment() {

    private var _binding: FragmentWorkstationBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val workstationViewModel = ViewModelProvider(this)[WorkstationViewModel::class.java]

        _binding = FragmentWorkstationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textWorkstation
        workstationViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}