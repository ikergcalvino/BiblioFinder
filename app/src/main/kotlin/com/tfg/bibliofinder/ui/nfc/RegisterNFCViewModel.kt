package com.tfg.bibliofinder.ui.nfc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterNFCViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Register NFC Fragment"
    }
    val text: LiveData<String> = _text
}