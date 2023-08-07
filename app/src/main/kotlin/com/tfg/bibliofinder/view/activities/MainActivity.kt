package com.tfg.bibliofinder.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.ActivityMainBinding
import com.tfg.bibliofinder.model.data.local.database.AppDatabase
import com.tfg.bibliofinder.model.entities.Classroom
import com.tfg.bibliofinder.model.entities.Library
import com.tfg.bibliofinder.model.entities.Workstation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getInstance(this)
        val sharedPrefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        initializeData(sharedPrefs)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        val isLoggedIn = sharedPrefs.contains("loggedInUserId")

        updateDrawer(navView, isLoggedIn)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_library, R.id.nav_profile), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_login -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    true
                }

                else -> {
                    navController.navigate(menuItem.itemId)
                    drawerLayout.closeDrawers()
                    true
                }
            }
        }
    }

    private fun updateDrawer(navView: NavigationView, isLoggedIn: Boolean) {

        if (isLoggedIn) {
            navView.inflateMenu(R.menu.drawer_logged_in)
        } else {
            navView.inflateMenu(R.menu.drawer_not_logged_in)
        }

    }

    private fun initializeData(sharedPrefs: SharedPreferences) {
        val dataLoaded = sharedPrefs.getBoolean("dataLoaded", false)
        if (!dataLoaded) {
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

            CoroutineScope(Dispatchers.IO).launch {
                database.loadInitialData(libraries, classrooms, workstations)
                withContext(Dispatchers.Main) {
                    sharedPrefs.edit().putBoolean("dataLoaded", true).apply()
                }
            }
        }
    }

    private fun readJsonFile(resourceId: Int): String {
        return resources.openRawResource(resourceId).bufferedReader().use { it.readText() }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}