package com.tfg.bibliofinder.util

import android.content.SharedPreferences
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.entities.User
import com.tfg.bibliofinder.exceptions.EmailAlreadyInUseException
import com.tfg.bibliofinder.exceptions.InvalidCredentialsException
import com.tfg.bibliofinder.exceptions.InvalidEmailFormatException
import com.tfg.bibliofinder.exceptions.InvalidPasswordException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mindrot.jbcrypt.BCrypt

class AuthenticationManager : KoinComponent {

    private val database: AppDatabase by inject()
    private val sharedPrefs: SharedPreferences by inject()

    suspend fun registerUser(email: String, password: String) {
        isValidEmail(email)
        isValidPassword(password)
        checkEmail(email)

        val newUser = User(email = email, password = hashPassword(password))
        database.userDao().insertUser(newUser)
    }

    suspend fun performLogin(email: String, password: String) {
        val user = database.userDao().getUserByEmail(email)

        if (isValidCredentials(email, password)) {
            with(sharedPrefs.edit()) {
                putLong("userId", user!!.userId)
                putString("userName", user.name)
                putString("userEmail", user.email)
                apply()
            }
        }
    }

    fun performLogout() {
        with(sharedPrefs.edit()) {
            remove("userId")
            remove("userName")
            remove("userEmail")
            apply()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        if (!Regex("^[A-Za-z0-9+_.-]+@(.+)\$").matches(email)) {
            throw InvalidEmailFormatException()
        }
        return true
    }

    private fun isValidPassword(password: String): Boolean {
        if (!Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{12,}\$").matches(password)) {
            throw InvalidPasswordException()
        }
        return true
    }

    private suspend fun checkEmail(email: String) {
        val user = database.userDao().getUserByEmail(email)

        if (user != null) {
            throw EmailAlreadyInUseException()
        }
    }

    private fun hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())

    private suspend fun isValidCredentials(email: String, password: String): Boolean {
        val user = database.userDao().getUserByEmail(email)

        if (user != null && BCrypt.checkpw(password, user.password)) {
            return true
        } else {
            throw InvalidCredentialsException()
        }
    }
}