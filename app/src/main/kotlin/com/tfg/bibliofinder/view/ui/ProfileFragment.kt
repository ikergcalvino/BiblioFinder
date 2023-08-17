package com.tfg.bibliofinder.view.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.FragmentProfileBinding
import com.tfg.bibliofinder.model.data.local.database.AppDatabase
import com.tfg.bibliofinder.model.entities.Workstation
import com.tfg.bibliofinder.model.util.MessageUtil
import com.tfg.bibliofinder.view.activities.NfcActivity
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

        viewModel.loadWorkstationDetails(loggedInUserId)

        viewModel.workstation.observe(viewLifecycleOwner) { workstation ->
            if (workstation?.userId == null) {
                binding.infoText.text = getString(R.string.current_booking)

                binding.emptyCard.visibility = View.VISIBLE
                binding.bookedCard.visibility = View.GONE
                binding.occupiedCard.visibility = View.GONE
            } else {
                if (workstation.state == Workstation.WorkstationState.BOOKED) {
                    binding.infoText.text = getString(R.string.current_booking)

                    binding.emptyCard.visibility = View.GONE
                    binding.bookedCard.visibility = View.VISIBLE
                    binding.occupiedCard.visibility = View.GONE

                    binding.bookedTitle.text = viewModel.libraryName.value
                    binding.bookedText.text = viewModel.classroomName.value
                } else if (workstation.state == Workstation.WorkstationState.OCCUPIED) {
                    binding.infoText.text = getString(R.string.occupied_site)

                    binding.emptyCard.visibility = View.GONE
                    binding.bookedCard.visibility = View.GONE
                    binding.occupiedCard.visibility = View.VISIBLE

                    binding.occupiedTitle.text = viewModel.libraryName.value
                    binding.occupiedText.text = viewModel.classroomName.value
                }
            }
        }

        binding.saveChangesButton.setOnClickListener {
            val newName = binding.usernameValue.text.toString()
            val newPhone = binding.phoneValue.text.toString()

            viewModel.updateUserDetails(loggedInUserId, newName, newPhone)

            val message = "Data saved successfully."
            MessageUtil.showToast(requireContext(), message)
        }

        binding.nfcButton.setOnClickListener {
            val intent = Intent(requireContext(), NfcActivity::class.java)
            startActivity(intent)
        }

        binding.leaveButton.setOnClickListener {
            // Update the Workstation parameters
            val newWorkstation = viewModel.workstation.value?.copy(userId = null)
            viewModel.updateWorkstationDetails(newWorkstation)

            // Refresh the fragment by replacing it with itself
            findNavController().navigate(R.id.nav_profile)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}