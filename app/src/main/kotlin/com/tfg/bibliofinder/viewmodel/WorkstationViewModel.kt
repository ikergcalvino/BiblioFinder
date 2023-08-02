package com.tfg.bibliofinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkstationViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is workstation Fragment"
    }
    val text: LiveData<String> = _text
}