package com.tfg.bibliofinder.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tfg.bibliofinder.databinding.ActivityRegistrationBinding
import com.tfg.bibliofinder.model.entities.User
import com.tfg.bibliofinder.model.util.AuthenticationManager
import com.tfg.bibliofinder.model.util.MessageUtil
import kotlinx.coroutines.launch

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var authenticationManager: AuthenticationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authenticationManager = AuthenticationManager(this)

        binding.buttonRegister.setOnClickListener {
            val email = binding.textEmail.text.toString()
            val password = binding.textPassword.text.toString()

            lifecycleScope.launch {
                registerUser(email, password)
            }
        }
    }

    private suspend fun registerUser(email: String, password: String) {
        if (!authenticationManager.isValidEmail(email)) {
            MessageUtil.showSnackbar(binding.root, "Invalid email format")
        } else if (!authenticationManager.isPasswordValid(password)) {
            MessageUtil.showSnackbar(
                binding.root,
                "Password must have at least 12 characters including uppercase, lowercase, and numbers"
            )
        } else {
            val user = authenticationManager.getUserByEmail(email)
            if (user != null) {
                MessageUtil.showSnackbar(binding.root, "Email is already in use")
            } else {
                val newUser =
                    User(email = email, password = authenticationManager.hashPassword(password))
                authenticationManager.insertUser(newUser)
                authenticationManager.performLogin(email, password)
                MessageUtil.showToast(applicationContext, "Registration successful. Welcome!")
                finish()
            }
        }
    }
}