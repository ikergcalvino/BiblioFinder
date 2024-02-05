package com.tfg.bibliofinder.util

import android.content.SharedPreferences
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.entities.User
import com.tfg.bibliofinder.exceptions.EmailAlreadyInUseException
import com.tfg.bibliofinder.exceptions.EmptyCredentialsException
import com.tfg.bibliofinder.exceptions.InvalidCredentialsException
import com.tfg.bibliofinder.exceptions.InvalidEmailFormatException
import com.tfg.bibliofinder.exceptions.InvalidPasswordException
import com.tfg.bibliofinder.exceptions.PasswordMismatchException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mindrot.jbcrypt.BCrypt

class AuthenticationManager : KoinComponent {

    private val database: AppDatabase by inject()
    private val sharedPrefs: SharedPreferences by inject()

    suspend fun registerUser(email: String, password: String, repeatPassword: String) {
        if (!isValidEmail(email)) {
            throw InvalidEmailFormatException()
        }

        if (!isValidPassword(password)) {
            throw InvalidPasswordException()
        }

        val user = database.userDao().getUserByEmail(email)

        if (user != null) {
            throw EmailAlreadyInUseException()
        }

        if (password != repeatPassword) {
            throw PasswordMismatchException()
        }

        val newUser = User(email = email, password = hashPassword(password))
        database.userDao().insertUser(newUser)
    }

    suspend fun logIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            throw EmptyCredentialsException()
        }

        val user = database.userDao().getUserByEmail(email)

        if (isValidCredentials(email, password)) {
            with(sharedPrefs.edit()) {
                putLong(Constants.USER_ID, user!!.userId)
                putString(Constants.USER_NAME, user.name)
                putString(Constants.USER_EMAIL, user.email)
                apply()
            }
        } else {
            throw InvalidCredentialsException()
        }
    }

    fun logOut() {
        with(sharedPrefs.edit()) {
            remove(Constants.USER_ID)
            remove(Constants.USER_NAME)
            remove(Constants.USER_EMAIL)
            apply()
        }
    }

    private fun isValidEmail(email: String): Boolean = email.matches(Regex(Constants.EMAIL_REGEX))

    private fun isValidPassword(password: String): Boolean =
        password.matches(Regex(Constants.PASSWORD_REGEX))

    private fun hashPassword(password: String, salt: String = BCrypt.gensalt()): String =
        BCrypt.hashpw(password, salt)

    private suspend fun isValidCredentials(email: String, password: String): Boolean {
        val user = database.userDao().getUserByEmail(email)

        return user != null && BCrypt.checkpw(password, user.password)
    }
}