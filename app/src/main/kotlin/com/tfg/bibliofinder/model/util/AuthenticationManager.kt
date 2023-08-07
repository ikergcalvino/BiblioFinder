package com.tfg.bibliofinder.model.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.tfg.bibliofinder.model.data.local.database.AppDatabase
import com.tfg.bibliofinder.model.entities.User
import com.tfg.bibliofinder.view.activities.MainActivity

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
        return user != null && user.password == password
    }

    suspend fun performLogin(username: String, password: String) {
        val user = database.userDao().getUserByEmail(username)
        if (user != null && user.password == password) {
            sharedPreferences.edit().putLong("loggedInUserId", user.userId).apply()

            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    fun performLogout() {
        sharedPreferences.edit().remove("loggedInUserId").apply()

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}