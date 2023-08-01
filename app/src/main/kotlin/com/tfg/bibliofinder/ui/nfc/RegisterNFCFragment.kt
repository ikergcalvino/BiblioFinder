package com.tfg.bibliofinder.ui.nfc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tfg.bibliofinder.databinding.FragmentRegisterNfcBinding

class RegisterNFCFragment : Fragment() {

    private var _binding: FragmentRegisterNfcBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RegisterNFCViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterNfcBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(this)[RegisterNFCViewModel::class.java]
        val textView: TextView = binding.textRegisterNfc
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
