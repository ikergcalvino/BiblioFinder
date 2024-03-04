package com.tfg.bibliofinder.screens.profile

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.entities.User
import com.tfg.bibliofinder.entities.Workstation
import com.tfg.bibliofinder.services.WorkstationService
import com.tfg.bibliofinder.util.Constants
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProfileViewModel : ViewModel(), KoinComponent {
    private val database: AppDatabase by inject()
    private val sharedPrefs: SharedPreferences by inject()
    private val workstationService: WorkstationService by inject()

    var libraryName: String? = ""
    var classroomName: String? = ""
    var user = MutableLiveData<User?>()
    var workstation = MutableLiveData<Workstation?>()

    suspend fun loadUserAndWorkstationData() {
        val userId = sharedPrefs.getLong(Constants.USER_ID, 0L)
        val user = database.userDao().getUserById(userId)

        val workstation = database.workstationDao().getWorkstationByUser(userId)
        val classroom =
            workstation?.classroomId?.let { database.classroomDao().getClassroomById(it) }
        val library = classroom?.libraryId?.let { database.libraryDao().getLibraryById(it) }

        libraryName = library?.name
        classroomName = classroom?.name
        this@ProfileViewModel.user.value = user
        this@ProfileViewModel.workstation.postValue(workstation)
    }

    suspend fun updateUserDetails(newName: String, newPhone: String) {
        sharedPrefs.edit().putString(Constants.USER_NAME, newName).apply()
        user.value?.apply {
            name = newName
            phone = newPhone
            database.userDao().updateUser(this)
        }
    }

    suspend fun updateWorkstationDetails() {
        workstation.value?.let { workstationService.releaseWorkstation(it) }
    }
}