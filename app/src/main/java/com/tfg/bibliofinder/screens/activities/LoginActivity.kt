package com.tfg.bibliofinder.screens.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.ActivityLoginBinding
import com.tfg.bibliofinder.services.AuthenticationService
import com.tfg.bibliofinder.services.exceptions.EmptyCredentialsException
import com.tfg.bibliofinder.services.exceptions.InvalidCredentialsException
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val authenticationService: AuthenticationService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogIn.setOnClickListener {
            binding.textEmail.error = null
            binding.textPasswordLayout.error = null

            val email = binding.textEmail.text.toString()
            val password = binding.textPassword.text.toString()

            lifecycleScope.launch {
                try {
                    authenticationService.logIn(email, password)

                    Toast.makeText(
                        applicationContext,
                        getString(R.string.log_in_successful),
                        Toast.LENGTH_SHORT
                    ).show()

                    val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(mainIntent)

                    finish()
                } catch (e: EmptyCredentialsException) {
                    binding.textEmail.error = getString(R.string.empty_credentials)
                } catch (e: InvalidCredentialsException) {
                    binding.textPasswordLayout.error = getString(R.string.invalid_credentials)
                }
            }
        }

        binding.createNewAccount.setOnClickListener {
            val registrationIntent = Intent(this, RegistrationActivity::class.java)
            startActivity(registrationIntent)
        }
    }
}