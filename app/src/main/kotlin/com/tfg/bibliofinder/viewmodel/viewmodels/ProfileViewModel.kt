package com.tfg.bibliofinder.viewmodel.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.bibliofinder.model.data.local.database.AppDatabase
import com.tfg.bibliofinder.model.entities.User
import com.tfg.bibliofinder.model.entities.Workstation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val database: AppDatabase) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: MutableLiveData<User?> = _user

    private val _workstation = MutableLiveData<Workstation?>()
    val workstation: MutableLiveData<Workstation?> = _workstation

    private val _libraryName = MutableLiveData<String?>()
    val libraryName: MutableLiveData<String?> = _libraryName

    private val _classroomName = MutableLiveData<String?>()
    val classroomName: MutableLiveData<String?> = _classroomName

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

    fun loadWorkstationDetails(userId: Long) {
        viewModelScope.launch {
            val workstation = database.workstationDao().getWorkstationByUser(userId)
            if (workstation != null) {
                val classroom = database.classroomDao().getClassroomById(workstation.classroomId)
                _classroomName.postValue(classroom?.name)

                val library = classroom?.libraryId?.let { database.libraryDao().getLibraryById(it) }
                _libraryName.postValue(library?.name)
            }
            _workstation.postValue(workstation)
        }
    }

    fun updateWorkstationDetails(updatedWorkstation: Workstation?) {
        viewModelScope.launch(Dispatchers.IO) {
            updatedWorkstation?.let { database.workstationDao().updateWorkstation(it) }
        }
    }
}