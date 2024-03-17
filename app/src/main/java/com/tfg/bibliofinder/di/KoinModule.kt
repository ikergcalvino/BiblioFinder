package com.tfg.bibliofinder.di

import android.content.Context
import android.content.SharedPreferences
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.screens.classroom.ClassroomViewModel
import com.tfg.bibliofinder.screens.library.LibraryViewModel
import com.tfg.bibliofinder.screens.profile.ProfileViewModel
import com.tfg.bibliofinder.screens.workstation.WorkstationViewModel
import com.tfg.bibliofinder.services.AuthenticationService
import com.tfg.bibliofinder.services.NotificationService
import com.tfg.bibliofinder.services.WorkstationService
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val biblioFinderModule = module {
    single<AppDatabase> {
        AppDatabase.getInstance(androidContext())
    }

    single<AuthenticationService> {
        AuthenticationService()
    }

    single<NotificationService> {
        NotificationService(androidContext())
    }

    single<WorkstationService> {
        WorkstationService()
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    }

    viewModel { LibraryViewModel() }
    viewModel { ClassroomViewModel() }
    viewModel { ProfileViewModel() }
    viewModel { WorkstationViewModel() }
}