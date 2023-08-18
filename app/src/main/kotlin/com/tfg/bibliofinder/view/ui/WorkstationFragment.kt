package com.tfg.bibliofinder.view.ui

import android.app.TimePickerDialog
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

    private var _binding: FragmentWorkstationBinding? = null
    private lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var workstationViewModel: WorkstationViewModel
    private lateinit var adapter: WorkstationAdapter
    private val workstations = mutableListOf<Workstation>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        database = AppDatabase.getInstance(requireContext())

        workstationViewModel = ViewModelFactory.createViewModel(database)

        _binding = FragmentWorkstationBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = WorkstationAdapter(workstations) { workstation ->
            showTimePickerDialog(workstation)
        }

        recyclerView.adapter = adapter

        val classroomId = arguments?.getLong("classroomId", -1L)
        if (classroomId != null && classroomId != -1L) {
            workstationViewModel.loadOpeningAndClosingTime(classroomId)
            workstationViewModel.getWorkstationsInClassroom(classroomId)
                .observe(viewLifecycleOwner) { workstations ->
                    this.workstations.clear()
                    this.workstations.addAll(workstations)
                    adapter.notifyDataSetChanged()
                }
        }

        return binding.root
    }

    private fun showTimePickerDialog(workstation: Workstation) {
        val libraryOpeningTime = workstationViewModel.openingTime
        val libraryClosingTime = workstationViewModel.closingTime

        val minHour: Int
        val minMinute: Int
        val maxHour: Int
        val maxMinute: Int

        if (libraryOpeningTime != null && libraryClosingTime != null) {
            val libraryOpeningHour = libraryOpeningTime.substringBefore(":").toInt()
            val libraryOpeningMinute = libraryOpeningTime.substringAfter(":").toInt()

            val libraryClosingHour = libraryClosingTime.substringBefore(":").toInt()
            val libraryClosingMinute = libraryClosingTime.substringAfter(":").toInt()

            minHour = libraryOpeningHour
            minMinute = libraryOpeningMinute

            maxHour = libraryClosingHour
            maxMinute = libraryClosingMinute
        } else {
            minHour = 0
            minMinute = 0
            maxHour = 23
            maxMinute = 59
        }

        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(), { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                if ((selectedHour > minHour || (selectedHour == minHour && selectedMinute >= minMinute)) && (selectedHour < maxHour || (selectedHour == maxHour && selectedMinute <= maxMinute))) {
                    // Resto de tu código para usar el tiempo seleccionado
                    workstationViewModel.reserveWorkstation(workstation)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}