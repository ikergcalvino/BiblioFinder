package com.tfg.bibliofinder.view.adapters

import android.view.View
import android.widget.TextView
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.model.entities.Workstation

class WorkstationAdapter(
    workstations: List<Workstation>, onBookClick: (Workstation) -> Unit
) : BaseAdapter<Workstation>(workstations, null, onBookClick, R.layout.card_workstation) {

    override fun bindItem(view: View, item: Workstation) {
        val workstationTitle: TextView = view.findViewById(R.id.workstation_title)
        workstationTitle.text = item.workstationId.toString()

        val workstationText: TextView = view.findViewById(R.id.workstation_state)
        when (item.state) {
            Workstation.WorkstationState.AVAILABLE -> {
                workstationText.setTextColor(view.context.getColor(R.color.available))
                workstationText.text = view.context.getString(R.string.AVAILABLE)
            }

            Workstation.WorkstationState.OCCUPIED -> {
                workstationText.setTextColor(view.context.getColor(R.color.occupied))
                workstationText.text = view.context.getString(R.string.OCCUPIED)
            }

            Workstation.WorkstationState.BOOKED -> {
                workstationText.setTextColor(view.context.getColor(R.color.booked))
                workstationText.text = view.context.getString(R.string.BOOKED)
            }
        }
    }
}