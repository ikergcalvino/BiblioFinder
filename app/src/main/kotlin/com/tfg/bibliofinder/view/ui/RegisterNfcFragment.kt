package com.tfg.bibliofinder.view.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tfg.bibliofinder.databinding.FragmentRegisterNfcBinding
import com.tfg.bibliofinder.viewmodel.RegisterNfcViewModel

class RegisterNfcFragment : Fragment() {

    private var _binding: FragmentRegisterNfcBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RegisterNfcViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView: TextView = binding.textRegisterNfc
        viewModel.nfcData.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}