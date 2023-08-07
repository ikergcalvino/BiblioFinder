package com.tfg.bibliofinder.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tfg.bibliofinder.databinding.ActivityRegistrationBinding
import com.tfg.bibliofinder.model.entities.User
import com.tfg.bibliofinder.model.util.AuthenticationManager
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
                val existingUser = authenticationManager.getUserByEmail(email)

                if (existingUser != null) {
                    // User already exists, show an error or handle accordingly
                } else {
                    val newUser = User(email = email, password = password)
                    authenticationManager.insertUser(newUser)

                    authenticationManager.performLogin(email, password)
                    finish()
                }
            }
        }
    }
}