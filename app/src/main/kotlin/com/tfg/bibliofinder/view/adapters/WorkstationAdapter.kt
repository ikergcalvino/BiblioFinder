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
        val workstationText: TextView = view.findViewById(R.id.workstation_text)

        workstationTitle.text = item.workstationId.toString()
        workstationText.text = item.state.toString()
    }
}