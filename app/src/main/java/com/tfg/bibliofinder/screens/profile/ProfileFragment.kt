package com.tfg.bibliofinder.screens.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.FragmentProfileBinding
import com.tfg.bibliofinder.entities.Workstation
import com.tfg.bibliofinder.screens.NfcActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadUserAndWorkstationData()
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.usernameValue.setText(user?.name ?: "")
            binding.emailValue.text = user?.email
            binding.phoneValue.setText(user?.phone ?: "")
        }

        viewModel.workstation.observe(viewLifecycleOwner) { workstation ->
            when {
                workstation?.userId == null -> {
                    binding.infoText.text = getString(R.string.current_booking)

                    binding.emptyCard.visibility = View.VISIBLE
                }

                workstation.state == Workstation.WorkstationState.BOOKED -> {
                    binding.infoText.text = getString(R.string.current_booking)
                    binding.bookedTitle.text = viewModel.libraryName
                    binding.bookedText.text = viewModel.classroomName

                    binding.bookedCard.visibility = View.VISIBLE
                }

                workstation.state == Workstation.WorkstationState.OCCUPIED -> {
                    binding.infoText.text = getString(R.string.occupied_site)
                    binding.occupiedTitle.text = viewModel.libraryName
                    binding.occupiedText.text = viewModel.classroomName

                    binding.occupiedCard.visibility = View.VISIBLE
                }
            }
        }

        binding.saveChangesButton.setOnClickListener {
            val newName = binding.usernameValue.text.toString()
            val newPhone = binding.phoneValue.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                viewModel.updateUserDetails(newName, newPhone)
            }

            Toast.makeText(requireContext(), "Data saved successfully.", Toast.LENGTH_SHORT).show()
        }

        val nfcButtonClickListener = View.OnClickListener {
            val nfcIntent = Intent(requireContext(), NfcActivity::class.java)
            startActivity(nfcIntent)
        }

        binding.emptyNfcButton.setOnClickListener(nfcButtonClickListener)
        binding.bookedNfcButton.setOnClickListener(nfcButtonClickListener)

        val exitButtonClickListener = View.OnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.updateWorkstationDetails()
            }

            findNavController().navigate(R.id.nav_profile)
        }

        binding.cancelButton.setOnClickListener(exitButtonClickListener)
        binding.leaveButton.setOnClickListener(exitButtonClickListener)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}