package com.tfg.bibliofinder.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tfg.bibliofinder.databinding.FragmentWorkstationBinding
import com.tfg.bibliofinder.model.data.local.database.AppDatabase
import com.tfg.bibliofinder.viewmodel.ViewModelFactory
import com.tfg.bibliofinder.viewmodel.viewmodels.WorkstationViewModel

class WorkstationFragment : Fragment() {

    private var _binding: FragmentWorkstationBinding? = null
    private lateinit var database: AppDatabase

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        database = AppDatabase.getInstance(requireContext())

        val workstationViewModel =
            ViewModelProvider(this, ViewModelFactory(database))[WorkstationViewModel::class.java]

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