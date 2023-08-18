package com.tfg.bibliofinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.bibliofinder.model.data.local.database.AppDatabase
import com.tfg.bibliofinder.viewmodel.viewmodels.ClassroomViewModel
import com.tfg.bibliofinder.viewmodel.viewmodels.LibraryViewModel
import com.tfg.bibliofinder.viewmodel.viewmodels.ProfileViewModel
import com.tfg.bibliofinder.viewmodel.viewmodels.WorkstationViewModel

class ViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LibraryViewModel::class.java) -> {
                LibraryViewModel(database) as T
            }

            modelClass.isAssignableFrom(ClassroomViewModel::class.java) -> {
                ClassroomViewModel(database) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(database) as T
            }

            modelClass.isAssignableFrom(WorkstationViewModel::class.java) -> {
                WorkstationViewModel(database) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        // Función genérica para crear ViewModels con la base de datos
        inline fun <reified T : ViewModel> createViewModel(database: AppDatabase): T {
            return ViewModelFactory(database).create(T::class.java)
        }
    }
}