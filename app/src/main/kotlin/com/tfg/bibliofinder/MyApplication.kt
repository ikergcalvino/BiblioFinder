package com.tfg.bibliofinder

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.di.biblioFinderModule
import com.tfg.bibliofinder.entities.Classroom
import com.tfg.bibliofinder.entities.Library
import com.tfg.bibliofinder.entities.Workstation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    private val database: AppDatabase by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(biblioFinderModule)
        }

        initializeData()
    }

    private fun initializeData() {
        CoroutineScope(Dispatchers.IO).launch {
            val gson = Gson()

            val librariesJson = readJsonFile(R.raw.libraries)
            val librariesType = object : TypeToken<List<Library>>() {}.type
            val libraries = gson.fromJson<List<Library>>(librariesJson, librariesType)

            val classroomsJson = readJsonFile(R.raw.classrooms)
            val classroomsType = object : TypeToken<List<Classroom>>() {}.type
            val classrooms = gson.fromJson<List<Classroom>>(classroomsJson, classroomsType)

            val workstationsJson = readJsonFile(R.raw.workstations)
            val workstationsType = object : TypeToken<List<Workstation>>() {}.type
            val workstations = gson.fromJson<List<Workstation>>(workstationsJson, workstationsType)

            withContext(Dispatchers.Main) {
                database.loadInitialData(libraries, classrooms, workstations)
            }
        }
    }

    private fun readJsonFile(resourceId: Int): String {
        return resources.openRawResource(resourceId).bufferedReader().use { it.readText() }
    }
}