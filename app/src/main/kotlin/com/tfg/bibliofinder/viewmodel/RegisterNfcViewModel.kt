package com.tfg.bibliofinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterNfcViewModel : ViewModel() {

    private val _nfcData = MutableLiveData<String>()

    val nfcData: LiveData<String> = _nfcData

    // This function will be called when NFC data is detected
    fun setNfcData(data: String) {
        _nfcData.value = data
    }
}