package com.tfg.bibliofinder.screens.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.ActivityLoginBinding
import com.tfg.bibliofinder.exceptions.InvalidCredentialsException
import com.tfg.bibliofinder.util.AuthenticationManager
import com.tfg.bibliofinder.util.MessageUtil
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val authManager: AuthenticationManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            attemptLogin()
        }

        binding.createNewAccount.setOnClickListener {
            val registrationIntent = Intent(this, RegistrationActivity::class.java)
            startActivity(registrationIntent)
        }
    }

    private fun attemptLogin() {
        val username = binding.textEmail.text.toString()
        val password = binding.textPassword.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            MessageUtil.showSnackbar(binding.root, getString(R.string.please_enter_email_password))
            return
        }

        lifecycleScope.launch {
            try {
                authManager.performLogin(username, password)

                val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(mainIntent)

                MessageUtil.showToast(applicationContext, getString(R.string.log_in_successful))

                finish()
            } catch (e: InvalidCredentialsException) {
                MessageUtil.showSnackbar(binding.root, getString(R.string.invalid_credentials))
            }
        }
    }
}