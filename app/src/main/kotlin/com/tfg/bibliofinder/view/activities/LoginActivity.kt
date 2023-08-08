package com.tfg.bibliofinder.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tfg.bibliofinder.databinding.ActivityLoginBinding
import com.tfg.bibliofinder.model.util.AuthenticationManager
import com.tfg.bibliofinder.model.util.MessageUtil
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authenticationManager: AuthenticationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authenticationManager = AuthenticationManager(this)

        binding.buttonLogin.setOnClickListener {
            val username = binding.textEmail.text.toString()
            val password = binding.textPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                MessageUtil.showSnackbar(binding.root, "Please enter your email and password")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                if (authenticationManager.isValidCredentials(username, password)) {
                    authenticationManager.performLogin(username, password)
                    MessageUtil.showToast(applicationContext, "You have successfully logged in")
                    finish()
                } else {
                    MessageUtil.showSnackbar(binding.root, "Incorrect email or password")
                }
            }
        }

        binding.createNewAccount.setOnClickListener {
            val registrationIntent = Intent(this, RegistrationActivity::class.java)
            startActivity(registrationIntent)
        }
    }
}