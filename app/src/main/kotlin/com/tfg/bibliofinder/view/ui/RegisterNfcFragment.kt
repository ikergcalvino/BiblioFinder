package com.tfg.bibliofinder.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tfg.bibliofinder.databinding.FragmentRegisterNfcBinding
import com.tfg.bibliofinder.viewmodel.RegisterNfcViewModel

class RegisterNfcFragment : Fragment() {

    private var _binding: FragmentRegisterNfcBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val registerNfcViewModel = ViewModelProvider(this)[RegisterNfcViewModel::class.java]

        _binding = FragmentRegisterNfcBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textRegisterNfc
        registerNfcViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}