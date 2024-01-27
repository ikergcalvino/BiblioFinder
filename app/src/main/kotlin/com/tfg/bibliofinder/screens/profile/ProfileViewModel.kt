package com.tfg.bibliofinder.screens.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.entities.User
import com.tfg.bibliofinder.entities.Workstation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProfileViewModel : ViewModel(), KoinComponent {
    private val database: AppDatabase by inject()

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
            val user = database.userDao().getUserById(userId)

            user?.name = newName
            user?.phone = newPhone

            user?.let { database.userDao().updateUser(it) }
        }
    }

    fun updateWorkstationDetails(workstation: Workstation?) {
        viewModelScope.launch(Dispatchers.IO) {
            workstation?.state = Workstation.WorkstationState.AVAILABLE
            workstation?.dateTime = null
            workstation?.userId = null

            workstation?.let { database.workstationDao().updateWorkstation(it) }
        }
    }
}