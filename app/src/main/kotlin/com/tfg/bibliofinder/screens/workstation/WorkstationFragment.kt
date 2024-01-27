package com.tfg.bibliofinder.screens.workstation

import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.FragmentWorkstationBinding
import com.tfg.bibliofinder.entities.Workstation
import com.tfg.bibliofinder.util.MessageUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class WorkstationFragment : Fragment() {

    private val workstations = mutableListOf<Workstation>()
    private var _binding: FragmentWorkstationBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WorkstationAdapter
    private lateinit var recyclerView: RecyclerView

    private val sharedPrefs: SharedPreferences by inject()
    private val viewModel: WorkstationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkstationBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = WorkstationAdapter(workstations) { workstation ->
            handleWorkstationReservation(workstation)
        }
        recyclerView.adapter = adapter

        val classroomId = arguments?.getLong("classroomId", 0L)
        if (classroomId != null && classroomId != 0L) {
            viewModel.loadOpeningAndClosingTime(classroomId)
            viewModel.getWorkstationsByClassroom(classroomId)
                .observe(viewLifecycleOwner) { workstations ->
                    this.workstations.clear()
                    this.workstations.addAll(workstations)
                    adapter.notifyDataSetChanged()
                }
        }

        return binding.root
    }

    private fun handleWorkstationReservation(workstation: Workstation) {
        val loggedInUserId = sharedPrefs.getLong("userId", 0L)

        if (loggedInUserId == 0L) {
            MessageUtil.showSnackbar(binding.root, getString(R.string.must_be_logged_in))
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            when {
                viewModel.hasUserBooking(loggedInUserId) -> {
                    MessageUtil.showSnackbar(
                        binding.root, getString(R.string.already_have_reservation)
                    )
                }

                workstation.state != Workstation.WorkstationState.AVAILABLE -> {
                    MessageUtil.showSnackbar(
                        binding.root, getString(R.string.workstation_already_occupied_reserved)
                    )
                }

                else -> reserveWorkstationTimeSlot(workstation, loggedInUserId)
            }
        }
    }

    private fun reserveWorkstationTimeSlot(workstation: Workstation, userId: Long) {
        val currentTime = Calendar.getInstance()
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentTime.get(Calendar.MINUTE)

        val libraryOpeningTime = viewModel.openingTime
        val libraryClosingTime = viewModel.closingTime

        val timePickerDialog = TimePickerDialog(
            requireContext(), { _, selectedHour, selectedMinute ->
                val selectedTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                }

                // Si la hora seleccionada está fuera del horario de apertura y cierre
                if (selectedTime.before(libraryOpeningTime) || selectedTime.after(libraryClosingTime)) {
                    MessageUtil.showSnackbar(
                        binding.root, getString(R.string.reservation_outside_allowed_hours)
                    )
                    return@TimePickerDialog
                }

                // Si la hora seleccionada ya pasó hoy, reservamos para el día siguiente
                if (selectedTime.before(currentTime)) {
                    selectedTime.add(Calendar.DAY_OF_MONTH, 1)
                }

                viewModel.reserveWorkstation(workstation, selectedTime.time.toString(), userId)
                MessageUtil.showToast(requireContext(), "OK")

            }, currentHour, currentMinute, true
        )

        timePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}