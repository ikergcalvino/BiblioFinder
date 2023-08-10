package com.tfg.bibliofinder.viewmodel.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.bibliofinder.model.data.local.database.AppDatabase
import com.tfg.bibliofinder.model.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val database: AppDatabase) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: MutableLiveData<User?> = _user

    fun loadUserData(userId: Long) {
        viewModelScope.launch {
            val user = database.userDao().getUserById(userId)
            _user.value = user
        }
    }

    fun updateUserDetails(userId: Long, newName: String, newPhone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = database.userDao().getUserById(userId)

            if (user != null) {
                user.name = newName
                user.phone = newPhone
                database.userDao().updateUser(user)
            }
        }
    }
}