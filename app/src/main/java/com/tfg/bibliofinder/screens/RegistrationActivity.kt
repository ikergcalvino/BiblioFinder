package com.tfg.bibliofinder.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.ActivityRegistrationBinding
import com.tfg.bibliofinder.model.AuthenticationManager
import com.tfg.bibliofinder.model.exceptions.EmailAlreadyInUseException
import com.tfg.bibliofinder.model.exceptions.InvalidEmailFormatException
import com.tfg.bibliofinder.model.exceptions.InvalidPasswordException
import com.tfg.bibliofinder.model.exceptions.PasswordMismatchException
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    private val authenticationManager: AuthenticationManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSignUp.setOnClickListener {
            binding.apply {
                textEmail.error = null
                textPasswordLayout.error = null
                textRepeatPasswordLayout.error = null
            }

            val email = binding.textEmail.text.toString()
            val password = binding.textPassword.text.toString()
            val repeatPassword = binding.textRepeatPassword.text.toString()

            lifecycleScope.launch {
                try {
                    authenticationManager.registerUser(email, password, repeatPassword)
                    authenticationManager.logIn(email, password)

                    Toast.makeText(
                        applicationContext,
                        getString(R.string.registration_successful),
                        Toast.LENGTH_SHORT
                    ).show()

                    val mainIntent = Intent(this@RegistrationActivity, MainActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                } catch (e: InvalidEmailFormatException) {
                    binding.textEmail.error = getString(R.string.invalid_email_format)
                } catch (e: InvalidPasswordException) {
                    binding.textPasswordLayout.error = getString(R.string.invalid_password)
                } catch (e: EmailAlreadyInUseException) {
                    binding.textEmail.error = getString(R.string.email_already_in_use)
                } catch (e: PasswordMismatchException) {
                    binding.textRepeatPasswordLayout.error = getString(R.string.password_mismatch)
                }
            }
        }
    }
}