package com.tfg.bibliofinder.screens.workstation

import android.view.View
import androidx.core.content.ContextCompat
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.CardWorkstationBinding
import com.tfg.bibliofinder.entities.Workstation
import com.tfg.bibliofinder.util.BaseAdapter
import com.tfg.bibliofinder.util.ItemClickListener

class WorkstationAdapter(
    workstations: List<Workstation>, clickListener: ItemClickListener<Workstation>
) : BaseAdapter<Workstation>(workstations, clickListener, R.layout.card_workstation) {

    override fun bindItem(view: View, item: Workstation) {
        val binding = CardWorkstationBinding.bind(view)
        val context = view.context

        binding.workstationTitle.text = context.getString(R.string.workstation, item.workstationId)

        val workstationText = binding.workstationState
        val (textResId, colorResId) = when (item.state) {
            Workstation.WorkstationState.AVAILABLE -> R.string.available to android.R.color.holo_green_light
            Workstation.WorkstationState.OCCUPIED -> R.string.occupied to android.R.color.holo_red_light
            Workstation.WorkstationState.BOOKED -> R.string.booked to android.R.color.holo_orange_light
        }

        workstationText.text = context.getString(textResId)
        workstationText.setTextColor(ContextCompat.getColor(context, colorResId))
    }
}