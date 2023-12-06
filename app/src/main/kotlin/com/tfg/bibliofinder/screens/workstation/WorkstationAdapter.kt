package com.tfg.bibliofinder.screens.workstation

import android.view.View
import androidx.core.content.ContextCompat
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.CardWorkstationBinding
import com.tfg.bibliofinder.entities.Workstation
import com.tfg.bibliofinder.util.BaseAdapter

class WorkstationAdapter(
    workstations: List<Workstation>, onBookClick: (Workstation) -> Unit
) : BaseAdapter<Workstation>(workstations, null, onBookClick, R.layout.card_workstation) {

    override fun bindItem(view: View, item: Workstation) {
        val binding = CardWorkstationBinding.bind(view)
        val context = view.context

        binding.workstationTitle.text = item.workstationId.toString()

        val workstationText = binding.workstationState
        val colorResId = when (item.state) {
            Workstation.WorkstationState.AVAILABLE -> R.color.available
            Workstation.WorkstationState.OCCUPIED -> R.color.occupied
            Workstation.WorkstationState.BOOKED -> R.color.booked
        }
        workstationText.setTextColor(ContextCompat.getColor(context, colorResId))
        workstationText.text = item.state.toString()
    }
}