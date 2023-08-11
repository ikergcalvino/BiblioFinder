package com.tfg.bibliofinder.view.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tfg.bibliofinder.databinding.FragmentProfileBinding
import com.tfg.bibliofinder.model.data.local.database.AppDatabase
import com.tfg.bibliofinder.model.util.MessageUtil
import com.tfg.bibliofinder.viewmodel.ViewModelFactory
import com.tfg.bibliofinder.viewmodel.viewmodels.ProfileViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var database: AppDatabase
    private lateinit var viewModel: ProfileViewModel
    private lateinit var sharedPrefs: SharedPreferences

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        database = AppDatabase.getInstance(requireContext())
        sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        viewModel =
            ViewModelProvider(this, ViewModelFactory(database))[ProfileViewModel::class.java]

        val loggedInUserId = sharedPrefs.getLong("loggedInUserId", 0)
        viewModel.loadUserData(loggedInUserId)

        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.usernameValue.setText(user.name ?: "")
                binding.emailValue.text = user.email
                binding.phoneValue.setText(user.phone ?: "")
            }
        }

        binding.saveChangesButton.setOnClickListener {
            val newName = binding.usernameValue.text.toString()
            val newPhone = binding.phoneValue.text.toString()

            viewModel.updateUserDetails(loggedInUserId, newName, newPhone)

            val message = "Data saved successfully."
            MessageUtil.showToast(requireContext(), message)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}