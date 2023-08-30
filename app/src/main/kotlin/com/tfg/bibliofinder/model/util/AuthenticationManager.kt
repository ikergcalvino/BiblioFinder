package com.tfg.bibliofinder.model.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.tfg.bibliofinder.model.data.local.database.AppDatabase
import com.tfg.bibliofinder.model.entities.User
import com.tfg.bibliofinder.view.activities.MainActivity
import org.mindrot.jbcrypt.BCrypt

class AuthenticationManager(private val context: Context) {

    private val database: AppDatabase = AppDatabase.getInstance(context)
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    fun isValidEmail(email: String): Boolean {
        val regexPattern = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")
        return regexPattern.matches(email)
    }

    fun isValidPassword(password: String): Boolean {
        val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{12,}\$")
        return passwordPattern.matches(password)
    }

    suspend fun getUserByEmail(email: String): User? {
        return database.userDao().getUserByEmail(email)
    }

    suspend fun insertUser(newUser: User) {
        database.userDao().insertUser(newUser)
    }

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    suspend fun isValidCredentials(email: String, password: String): Boolean {
        val user = database.userDao().getUserByEmail(email)
        return user != null && BCrypt.checkpw(password, user.password)
    }

    suspend fun performLogin(username: String, password: String) {
        val user = database.userDao().getUserByEmail(username)
        if (user != null && BCrypt.checkpw(password, user.password)) {
            sharedPreferences.edit().putLong("loggedInUserId", user.userId).apply()
            startMainActivity()
        }
    }

    fun performLogout() {
        sharedPreferences.edit().remove("loggedInUserId").apply()
        startMainActivity()
    }

    private fun startMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}