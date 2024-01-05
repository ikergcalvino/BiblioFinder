package com.tfg.bibliofinder.screens.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.ActivityMainBinding
import com.tfg.bibliofinder.util.AuthenticationManager
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val sharedPrefs: SharedPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        navView.inflateMenu(R.menu.main_drawer)

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

                R.id.nav_logout -> {
                    val authManager = AuthenticationManager(this)
                    authManager.performLogout()
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

    override fun onResume() {
        super.onResume()

        val isLoggedIn = sharedPrefs.contains("userId")
        updateDrawer(isLoggedIn)
    }

    private fun updateDrawer(isLoggedIn: Boolean) {
        val header = binding.navView.getHeaderView(0)
        val userName = header.findViewById<TextView>(R.id.user_name)
        val userEmail = header.findViewById<TextView>(R.id.user_email)

        userName.text = sharedPrefs.getString("userName", "")
        userEmail.text = sharedPrefs.getString("userEmail", "")

        val menu = binding.navView.menu
        menu.findItem(R.id.nav_login).isVisible = !isLoggedIn
        menu.findItem(R.id.nav_profile).isVisible = isLoggedIn
        menu.findItem(R.id.nav_logout).isVisible = isLoggedIn
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}