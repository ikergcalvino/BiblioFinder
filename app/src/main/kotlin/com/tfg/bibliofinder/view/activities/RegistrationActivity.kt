package com.tfg.bibliofinder.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tfg.bibliofinder.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Your registration activity initialization code here

        // Assuming you have a "Register" button in your registration layout
        binding.buttonRegister.setOnClickListener {
            // Perform registration logic here

            // After successful registration, call the login function
            performLogin()

            // Navigate back to the main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close the RegistrationActivity if needed
        }
    }

    private fun performLogin() {
        // Call your login logic here
        // You can reuse the isValidCredentials function or implement your own logic
    }
}