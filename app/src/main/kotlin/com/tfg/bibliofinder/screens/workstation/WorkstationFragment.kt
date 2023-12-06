package com.tfg.bibliofinder.screens.workstation

import android.app.TimePickerDialog
import android.content.Context
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
import org.koin.androidx.viewmodel.ext.android.viewModel

class WorkstationFragment : Fragment() {

    private val workstations = mutableListOf<Workstation>()
    private var _binding: FragmentWorkstationBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WorkstationAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPrefs: SharedPreferences
    private val viewModel: WorkstationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkstationBinding.inflate(inflater, container, false)

        sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

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
            viewModel.getWorkstationsInClassroom(classroomId)
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
            val hasBooking = viewModel.hasUserBooking(loggedInUserId)

            if (hasBooking) {
                MessageUtil.showSnackbar(binding.root, getString(R.string.already_have_reservation))
            } else {
                reserveWorkstationTimeSlot(workstation, loggedInUserId)
            }
        }
    }

    private fun reserveWorkstationTimeSlot(workstation: Workstation, userId: Long) {
        val (minHour, minMinute) = getLibraryTime(viewModel.openingTime, 0, 0)
        val (maxHour, maxMinute) = getLibraryTime(viewModel.closingTime, 23, 59)

        val currentTime = Calendar.getInstance()
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentTime.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(), { _, selectedHour, selectedMinute ->
                val selectedTime = getSelectedTime(selectedHour, selectedMinute)

                val libraryOpeningTime = getSelectedTime(minHour, minMinute)
                val libraryClosingTime = getSelectedTime(maxHour, maxMinute)

                if (selectedTime.before(libraryOpeningTime) || selectedTime.after(libraryClosingTime)) {
                    // Si la hora seleccionada está fuera del horario de apertura y cierre
                    MessageUtil.showSnackbar(
                        binding.root, getString(R.string.reservation_outside_allowed_hours)
                    )
                    return@TimePickerDialog
                }

                if (selectedTime.before(currentTime)) {
                    // Si la hora seleccionada ya pasó hoy, reservamos para el día siguiente
                    selectedTime.add(Calendar.DAY_OF_MONTH, 1)
                }

                workstation.dateTime = selectedTime.time.toString()

                if (workstation.state == Workstation.WorkstationState.AVAILABLE) {
                    viewModel.reserveWorkstation(workstation, userId)
                } else {
                    MessageUtil.showSnackbar(
                        binding.root, getString(R.string.workstation_already_occupied_or_reserved)
                    )
                }
            }, currentHour, currentMinute, true
        )
        timePickerDialog.show()
    }

    private fun getSelectedTime(selectedHour: Int, selectedMinute: Int): Calendar {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, selectedHour)
            set(Calendar.MINUTE, selectedMinute)
        }
    }

    private fun getLibraryTime(
        hourMinute: String?, defaultHour: Int, defaultMinute: Int
    ): Pair<Int, Int> {
        val hour = hourMinute?.substringBefore(":")?.toIntOrNull() ?: defaultHour
        val minute = hourMinute?.substringAfter(":")?.toIntOrNull() ?: defaultMinute
        return Pair(hour, minute)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}