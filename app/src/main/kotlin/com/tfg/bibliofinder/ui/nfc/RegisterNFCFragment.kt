package com.tfg.bibliofinder.ui.nfc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tfg.bibliofinder.R

class RegisterNFCFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterNFCFragment()
    }

    private lateinit var viewModel: RegisterNFCViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_nfc, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[RegisterNFCViewModel::class.java]
        // TODO: Use the ViewModel
    }

}