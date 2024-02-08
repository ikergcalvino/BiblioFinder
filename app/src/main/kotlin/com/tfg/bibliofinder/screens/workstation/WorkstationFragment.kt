package com.tfg.bibliofinder.screens.workstation

import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.FragmentWorkstationBinding
import com.tfg.bibliofinder.entities.Workstation
import com.tfg.bibliofinder.exceptions.UserAlreadyHasBookingException
import com.tfg.bibliofinder.exceptions.UserNotLoggedInException
import com.tfg.bibliofinder.exceptions.WorkstationNotAvailableException
import com.tfg.bibliofinder.util.Constants
import com.tfg.bibliofinder.util.ItemClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class WorkstationFragment : Fragment(), ItemClickListener<Workstation> {

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

        adapter = WorkstationAdapter(workstations, this)
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

    private suspend fun reserveWorkstationTimeSlot(workstation: Workstation) {
        val userId = sharedPrefs.getLong(Constants.USER_ID, 0L)

        if (userId == 0L) {
            throw UserNotLoggedInException()
        }

        when {
            viewModel.hasUserBooking(userId) -> {
                throw UserAlreadyHasBookingException()
            }

            workstation.state != Workstation.WorkstationState.AVAILABLE -> {
                throw WorkstationNotAvailableException()
            }

            else -> {
                val currentTime = Calendar.getInstance()
                val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
                val currentMinute = currentTime.get(Calendar.MINUTE)

                val libraryOpeningTime = viewModel.openingTime
                val libraryClosingTime = viewModel.closingTime

                val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(currentHour).setMinute(currentMinute)
                    .setTitleText("Selecciona la hora").build()

                timePicker.addOnPositiveButtonClickListener {
                    val selectedHour = timePicker.hour
                    val selectedMinute = timePicker.minute

                    val selectedTime = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, selectedHour)
                        set(Calendar.MINUTE, selectedMinute)
                    }

                    // Si la hora seleccionada está fuera del horario de apertura o cierre
                    if (selectedTime.before(libraryOpeningTime) || selectedTime.after(
                            libraryClosingTime
                        )
                    ) {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.booking_outside_allowed_hours),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        return@addOnPositiveButtonClickListener
                    }

                    // Si la hora seleccionada ya pasó hoy, reservamos para el día siguiente
                    if (selectedTime.before(currentTime)) {
                        selectedTime.add(Calendar.DAY_OF_MONTH, 1)
                    }

                    viewModel.reserveWorkstation(workstation, selectedTime.time.toString(), userId)

                    Toast.makeText(requireContext(), "OK", Toast.LENGTH_SHORT).show()
                }

                timePicker.show(parentFragmentManager, "tag")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(item: Workstation) {
        TODO("Not yet implemented")
    }

    override fun onInfoButtonClick(item: Workstation) {
        TODO("Not yet implemented")
    }

    override fun onBookButtonClick(item: Workstation) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                reserveWorkstationTimeSlot(item)
            } catch (e: UserNotLoggedInException) {
                Snackbar.make(
                    binding.root, getString(R.string.must_be_logged_in), Snackbar.LENGTH_SHORT
                ).show()
            } catch (e: UserAlreadyHasBookingException) {
                Snackbar.make(
                    binding.root, getString(R.string.already_have_booking), Snackbar.LENGTH_SHORT
                ).show()
            } catch (e: WorkstationNotAvailableException) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.workstation_already_occupied_reserved),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}