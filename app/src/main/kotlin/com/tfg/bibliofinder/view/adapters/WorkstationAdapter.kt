package com.tfg.bibliofinder.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.model.entities.Workstation

class WorkstationAdapter(
    private val workstations: List<Workstation>, private val onBookClick: (Workstation) -> Unit
) : RecyclerView.Adapter<WorkstationAdapter.WorkstationViewHolder>() {

    inner class WorkstationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var workstationTitle: TextView = itemView.findViewById(R.id.workstation_title)
        var workstationText: TextView = itemView.findViewById(R.id.workstation_text)
        var bookButton: ImageButton = itemView.findViewById(R.id.book_button)
    }

    override fun getItemCount(): Int {
        return workstations.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkstationViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.card_workstation, parent, false)
        return WorkstationViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkstationViewHolder, position: Int) {
        val workstation = workstations[position]

        holder.workstationTitle.text = workstation.workstationId.toString()
        holder.workstationText.text = workstation.state.toString()

        holder.bookButton.setOnClickListener {
            onBookClick(workstation)
        }
    }
}