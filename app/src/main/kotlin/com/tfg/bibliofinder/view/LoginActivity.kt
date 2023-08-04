package com.tfg.bibliofinder.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tfg.bibliofinder.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val username = binding.textEmail.text.toString()
            val password = binding.textPassword.text.toString()

            if (isValidCredentials(username, password)) {
                // Valid credentials, perform login logic here

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Optional: Close the LoginActivity if needed
            } else {
                //binding.textViewErrorMessage.visibility = View.VISIBLE
            }
        }

        // Add OnClickListener to navigate to registration activity
        binding.createNewAccount.setOnClickListener {
            val registrationIntent = Intent(this, RegistrationActivity::class.java)
            startActivity(registrationIntent)
        }
    }

    private fun isValidCredentials(username: String, password: String): Boolean {
        // You can implement your own logic for validating credentials here
        // For example, check against a database or predefined values
        return username.isNotEmpty() && password.isNotEmpty()
    }
}