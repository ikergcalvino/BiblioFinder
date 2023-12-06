package com.tfg.bibliofinder.screens.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.entities.User
import com.tfg.bibliofinder.entities.Workstation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val database: AppDatabase) : ViewModel() {

    val user = MutableLiveData<User?>()
    val workstation = MutableLiveData<Workstation?>()
    val libraryAndClassroom = MutableLiveData<Pair<String?, String?>>()

    fun loadUserAndWorkstationData(userId: Long) {
        viewModelScope.launch {
            val user = database.userDao().getUserById(userId)
            this@ProfileViewModel.user.value = user

            val workstation = database.workstationDao().getWorkstationByUser(userId)
            val classroom =
                workstation?.let { database.classroomDao().getClassroomById(it.classroomId) }
            val library = classroom?.libraryId?.let { database.libraryDao().getLibraryById(it) }

            libraryAndClassroom.postValue(Pair(library?.name, classroom?.name))
            this@ProfileViewModel.workstation.postValue(workstation)
        }
    }

    fun updateUserDetails(userId: Long, newName: String, newPhone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedUser =
                database.userDao().getUserById(userId)?.copy(name = newName, phone = newPhone)

            updatedUser?.let { database.userDao().updateUser(it) }
        }
    }

    fun updateWorkstationDetails(updatedWorkstation: Workstation?) {
        viewModelScope.launch(Dispatchers.IO) {
            updatedWorkstation?.let { database.workstationDao().updateWorkstation(it) }
        }
    }
}