package com.tfg.bibliofinder.screens.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.FragmentProfileBinding
import com.tfg.bibliofinder.entities.Workstation
import com.tfg.bibliofinder.util.MessageUtil
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val sharedPrefs: SharedPreferences by inject()
    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val loggedInUserId = sharedPrefs.getLong("userId", 0L)

        viewModel.loadUserAndWorkstationData(loggedInUserId)

        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.usernameValue.setText(user?.name ?: "")
            binding.emailValue.text = user?.email
            binding.phoneValue.setText(user?.phone ?: "")
        }

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

                    binding.bookedTitle.text = viewModel.libraryAndClassroom.value?.first
                    binding.bookedText.text = viewModel.libraryAndClassroom.value?.second
                } else if (workstation.state == Workstation.WorkstationState.OCCUPIED) {
                    binding.infoText.text = getString(R.string.occupied_site)

                    binding.emptyCard.visibility = View.GONE
                    binding.bookedCard.visibility = View.GONE
                    binding.occupiedCard.visibility = View.VISIBLE

                    binding.occupiedTitle.text = viewModel.libraryAndClassroom.value?.first
                    binding.occupiedText.text = viewModel.libraryAndClassroom.value?.second
                }
            }
        }

        binding.saveChangesButton.setOnClickListener {
            val newName = binding.usernameValue.text.toString()
            val newPhone = binding.phoneValue.text.toString()

            sharedPrefs.edit().putString("userName", newName).apply()

            viewModel.updateUserDetails(loggedInUserId, newName, newPhone)

            MessageUtil.showToast(requireContext(), "Data saved successfully.")
        }

        binding.nfcButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_nav_nfc)
        }

        binding.cancelButton.setOnClickListener {
            releaseWorkstation()
        }

        binding.leaveButton.setOnClickListener {
            releaseWorkstation()
        }

        return binding.root
    }

    private fun releaseWorkstation() {
        val updatedWorkstation = viewModel.workstation.value?.copy(
            state = Workstation.WorkstationState.AVAILABLE, dateTime = null, userId = null
        )
        viewModel.updateWorkstationDetails(updatedWorkstation)

        findNavController().navigate(R.id.nav_profile)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}