package com.tfg.bibliofinder.screens.workstation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.FragmentWorkstationBinding
import com.tfg.bibliofinder.entities.Workstation
import com.tfg.bibliofinder.services.exceptions.BookingOutsideAllowedHoursException
import com.tfg.bibliofinder.services.exceptions.UserAlreadyHasBookingException
import com.tfg.bibliofinder.services.exceptions.UserNotLoggedInException
import com.tfg.bibliofinder.services.exceptions.WorkstationNotAvailableException
import com.tfg.bibliofinder.util.ItemClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class WorkstationFragment : Fragment(), ItemClickListener<Workstation> {

    private var _binding: FragmentWorkstationBinding? = null
    private val binding get() = _binding!!

    private val workstations = mutableListOf<Workstation>()

    private val adapter: WorkstationAdapter by lazy { WorkstationAdapter(workstations, this) }
    private val viewModel: WorkstationViewModel by viewModel()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkstationBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@WorkstationFragment.adapter
        }

        val classroomId = arguments?.getLong("classroomId", 0L)
        if (classroomId != null && classroomId != 0L) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.initializeBookingsInClassroom(classroomId)
            }

            viewModel.getWorkstationsByClassroom(classroomId)
                .observe(viewLifecycleOwner) { workstations ->
                    this.workstations.clear()
                    this.workstations.addAll(workstations)
                    adapter.notifyDataSetChanged()
                }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(item: Workstation) {
        val workstationLocationIcon =
            view?.findViewById<ShapeableImageView>(R.id.workstation_location)

        workstationLocationIcon?.let {
            val layoutParams = it.layoutParams as FrameLayout.LayoutParams
            layoutParams.leftMargin = item.location.x
            layoutParams.topMargin = item.location.y
            it.layoutParams = layoutParams
            it.visibility = View.VISIBLE
        }
    }

    override fun onInfoButtonClick(item: Workstation) {
        TODO("Not yet implemented")
    }

    override fun onBookButtonClick(item: Workstation) {
        lifecycleScope.launch {
            try {
                viewModel.validateWorkstationBooking(item)

                val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(LocalTime.now().hour).setMinute(LocalTime.now().minute)
                    .setTitleText("Selecciona la hora").build()

                timePicker.addOnPositiveButtonClickListener {
                    val selectedTime = LocalDateTime.of(
                        LocalDate.now(), LocalTime.of(timePicker.hour, timePicker.minute)
                    )

                    lifecycleScope.launch {
                        try {
                            viewModel.bookWorkstationAtSelectedTime(item, selectedTime)

                            Toast.makeText(
                                requireContext(), selectedTime.toString(), Toast.LENGTH_SHORT
                            ).show()

                        } catch (e: BookingOutsideAllowedHoursException) {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.booking_outside_allowed_hours),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                timePicker.show(parentFragmentManager, "tag")

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
                    getString(R.string.workstation_already_occupied_booked),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}