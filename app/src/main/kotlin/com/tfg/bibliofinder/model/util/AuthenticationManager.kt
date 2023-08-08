package com.tfg.bibliofinder.model.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.tfg.bibliofinder.model.data.local.database.AppDatabase
import com.tfg.bibliofinder.model.entities.User
import com.tfg.bibliofinder.view.activities.MainActivity
import java.security.MessageDigest

class AuthenticationManager(private val context: Context) {

    private val database: AppDatabase = AppDatabase.getInstance(context)
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    suspend fun getUserByEmail(email: String): User? {
        return database.userDao().getUserByEmail(email)
    }

    suspend fun insertUser(newUser: User) {
        database.userDao().insertUser(newUser)
    }

    suspend fun isValidCredentials(username: String, password: String): Boolean {
        val user = database.userDao().getUserByEmail(username)
        return user != null && user.password == hashPassword(password)
    }

    suspend fun performLogin(username: String, password: String) {
        val user = database.userDao().getUserByEmail(username)
        if (user != null && user.password == hashPassword(password)) {
            sharedPreferences.edit().putLong("loggedInUserId", user.userId).apply()
            startMainActivity()
        }
    }

    fun performLogout() {
        sharedPreferences.edit().remove("loggedInUserId").apply()
        startMainActivity()
    }

    fun isValidEmail(email: String): Boolean {
        val regexPattern = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")
        return regexPattern.matches(email)
    }

    fun isPasswordValid(password: String): Boolean {
        val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{12,}\$")
        return passwordPattern.matches(password)
    }

    fun hashPassword(password: String): String {
        // TODO: Implement your preferred password hashing mechanism here
        // For example, you can use SHA-256 hashing algorithm.
        // Note: In practice, you should use a proper password hashing library like bcrypt.
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun startMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}