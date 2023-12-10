package com.tfg.bibliofinder.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.entities.User
import com.tfg.bibliofinder.screens.activities.MainActivity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mindrot.jbcrypt.BCrypt

class AuthenticationManager(private val context: Context) : KoinComponent {

    private val database: AppDatabase by inject()
    private val sharedPrefs: SharedPreferences by inject()

    fun isValidEmail(email: String): Boolean = Regex("^[A-Za-z0-9+_.-]+@(.+)\$").matches(email)

    fun isValidPassword(password: String): Boolean =
        Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{12,}\$").matches(password)

    suspend fun getUserByEmail(email: String): User? = database.userDao().getUserByEmail(email)

    suspend fun insertUser(newUser: User) = database.userDao().insertUser(newUser)

    fun hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())

    suspend fun isValidCredentials(email: String, password: String): Boolean {
        val user = getUserByEmail(email)
        return user != null && BCrypt.checkpw(password, user.password)
    }

    suspend fun performLogin(username: String, password: String) {
        val user = getUserByEmail(username)
        if (user != null && BCrypt.checkpw(password, user.password)) {
            with(sharedPrefs.edit()) {
                putLong("userId", user.userId)
                putString("userName", user.name)
                putString("userEmail", user.email)
                apply()
            }
            startMainActivity()
        }
    }

    fun performLogout() {
        with(sharedPrefs.edit()) {
            remove("userId")
            remove("userName")
            remove("userEmail")
            apply()
        }
        startMainActivity()
    }

    private fun startMainActivity() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }
}