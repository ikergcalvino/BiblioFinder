package com.tfg.bibliofinder.screens.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.ActivityRegistrationBinding
import com.tfg.bibliofinder.exceptions.EmailAlreadyInUseException
import com.tfg.bibliofinder.exceptions.InvalidEmailFormatException
import com.tfg.bibliofinder.exceptions.InvalidPasswordException
import com.tfg.bibliofinder.exceptions.PasswordMismatchException
import com.tfg.bibliofinder.services.AuthenticationService
import kotlinx.coroutines.launch

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    private val authenticationService: AuthenticationService = AuthenticationService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSignUp.setOnClickListener {
            binding.textEmail.error = null
            binding.textPasswordLayout.error = null
            binding.textRepeatPasswordLayout.error = null

            val email = binding.textEmail.text.toString()
            val password = binding.textPassword.text.toString()
            val repeatPassword = binding.textRepeatPassword.text.toString()

            lifecycleScope.launch {
                try {
                    authenticationService.registerUser(email, password, repeatPassword)
                    authenticationService.logIn(email, password)

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