package com.tfg.bibliofinder.screens.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textview.MaterialTextView
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.ActivityMainBinding
import com.tfg.bibliofinder.services.AuthenticationService
import com.tfg.bibliofinder.util.Constants
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val authenticationService: AuthenticationService by inject()
    private val sharedPrefs: SharedPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        selectDrawerMenu()

        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.nav_library, R.id.nav_profile), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_library -> {
                    navController.navigate(R.id.nav_library)
                    true
                }

                R.id.nav_login -> {
                    val loginIntent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(loginIntent)
                    true
                }

                R.id.nav_profile -> {
                    navController.navigate(R.id.nav_profile)
                    true
                }

                R.id.nav_logout -> {
                    authenticationService.logOut()
                    val logoutIntent = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(logoutIntent)
                    finish()
                    true
                }

                else -> false

            }.also { drawerLayout.closeDrawers() }
        }
    }

    override fun onResume() {
        super.onResume()
        selectDrawerMenu()
    }

    private fun selectDrawerMenu() {
        val headerView = binding.navView.getHeaderView(0)

        val userName = headerView.findViewById<MaterialTextView>(R.id.user_name)
        val userEmail = headerView.findViewById<MaterialTextView>(R.id.user_email)
        val profilePicture = headerView.findViewById<ShapeableImageView>(R.id.profile_picture)

        userName.text = sharedPrefs.getString(Constants.USER_NAME, getString(R.string.app_name))
        userEmail.text = sharedPrefs.getString(Constants.USER_EMAIL, getString(R.string.welcome))

        binding.navView.menu.clear()

        val isLoggedIn = sharedPrefs.contains(Constants.USER_ID)

        if (isLoggedIn) {
            binding.navView.inflateMenu(R.menu.drawer_menu_logged_in)
            profilePicture.visibility = View.VISIBLE
        } else {
            binding.navView.inflateMenu(R.menu.drawer_menu_logged_out)
            profilePicture.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}