package com.tfg.bibliofinder

import android.app.Application
import com.tfg.bibliofinder.di.biblioFinderModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(biblioFinderModule)
        }
    }
}