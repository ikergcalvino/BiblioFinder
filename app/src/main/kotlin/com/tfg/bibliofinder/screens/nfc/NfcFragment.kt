package com.tfg.bibliofinder.screens.nfc

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.FragmentNfcBinding

class NfcFragment : Fragment() {

    private var _binding: FragmentNfcBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNfcBinding.inflate(inflater, container, false)

        val isNightMode =
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        if (isNightMode) binding.nfcReader.setAnimation(R.raw.nfc_reader_night)
        else binding.nfcReader.setAnimation(R.raw.nfc_reader_day)

        binding.nfcReader.playAnimation()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}