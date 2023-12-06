package com.tfg.bibliofinder.di

import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.screens.classroom.ClassroomViewModel
import com.tfg.bibliofinder.screens.library.LibraryViewModel
import com.tfg.bibliofinder.screens.profile.ProfileViewModel
import com.tfg.bibliofinder.screens.workstation.WorkstationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val biblioFinderModule = module {
    single { AppDatabase.getInstance(get()) }

    viewModel { LibraryViewModel() }
    viewModel { ClassroomViewModel() }
    viewModel { ProfileViewModel() }
    viewModel { WorkstationViewModel() }
}