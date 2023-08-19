package com.tfg.bibliofinder.view.ui

import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tfg.bibliofinder.databinding.FragmentWorkstationBinding
import com.tfg.bibliofinder.model.data.local.database.AppDatabase
import com.tfg.bibliofinder.model.entities.Workstation
import com.tfg.bibliofinder.model.util.MessageUtil
import com.tfg.bibliofinder.view.adapters.WorkstationAdapter
import com.tfg.bibliofinder.viewmodel.ViewModelFactory
import com.tfg.bibliofinder.viewmodel.viewmodels.WorkstationViewModel

class WorkstationFragment : Fragment() {

    private val workstations = mutableListOf<Workstation>()
    private var _binding: FragmentWorkstationBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WorkstationAdapter
    private lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var viewModel: WorkstationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        database = AppDatabase.getInstance(requireContext())
        viewModel = ViewModelFactory.createViewModel(database)
        _binding = FragmentWorkstationBinding.inflate(inflater, container, false)
        sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = WorkstationAdapter(workstations) { workstation ->
            showTimePickerDialog(workstation)
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

    private fun showTimePickerDialog(workstation: Workstation) {
        val loggedInUserId = sharedPrefs.getLong("loggedInUserId", 0L)

        if (loggedInUserId != 0L) {

            val libraryOpeningTime = viewModel.openingTime
            val libraryClosingTime = viewModel.closingTime

            val minHour = libraryOpeningTime?.substringBefore(":")?.toInt() ?: 0
            val minMinute = libraryOpeningTime?.substringAfter(":")?.toInt() ?: 0

            val maxHour = libraryClosingTime?.substringBefore(":")?.toInt() ?: 23
            val maxMinute = libraryClosingTime?.substringAfter(":")?.toInt() ?: 59

            val currentTime = Calendar.getInstance()
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                requireContext(), { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                    if ((selectedHour > minHour || (selectedHour == minHour && selectedMinute >= minMinute)) && (selectedHour < maxHour || (selectedHour == maxHour && selectedMinute <= maxMinute))) {
                        if (workstation.state == Workstation.WorkstationState.AVAILABLE) {
                            viewModel.reserveWorkstation(workstation, loggedInUserId)
                        } else {
                            MessageUtil.showSnackbar(
                                binding.root, "Este sitio ya está ocupado o reservado previamente."
                            )
                        }
                    } else {
                        // Mostrar un mensaje de error o notificación si se selecciona una hora fuera de los límites.
                        MessageUtil.showSnackbar(
                            binding.root,
                            "La reserva no se puede realizar fuera del horario permitido."
                        )
                    }
                }, hour, minute, true
            )
            timePickerDialog.show()
        } else {
            MessageUtil.showSnackbar(
                binding.root, "Debes estar logueado para hacer una reserva."
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}