package com.tfg.bibliofinder.screens.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.ActivityRegistrationBinding
import com.tfg.bibliofinder.util.AuthenticationManager
import com.tfg.bibliofinder.util.MessageUtil
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    private val authManager: AuthenticationManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener {
            val email = binding.textEmail.text.toString()
            val password = binding.textPassword.text.toString()

            lifecycleScope.launch {
                registerUser(email, password)
            }
        }
    }

    private suspend fun registerUser(email: String, password: String) {
        when {
            !authManager.isValidEmail(email) -> {
                MessageUtil.showSnackbar(binding.root, getString(R.string.invalid_email_format))
            }

            !authManager.isValidPassword(password) -> {
                MessageUtil.showSnackbar(binding.root, getString(R.string.invalid_password_format))
            }

            authManager.getUserByEmail(email) != null -> {
                MessageUtil.showSnackbar(binding.root, getString(R.string.email_already_in_use))
            }

            else -> {
                authManager.insertUser(email, password)
                authManager.performLogin(email, password)

                MessageUtil.showToast(
                    applicationContext, getString(R.string.registration_successful)
                )

                val mainIntent = Intent(this@RegistrationActivity, MainActivity::class.java)
                startActivity(mainIntent)

                finish()
            }
        }
    }
}