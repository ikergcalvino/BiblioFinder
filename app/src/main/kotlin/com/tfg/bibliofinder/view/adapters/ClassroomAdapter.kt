package com.tfg.bibliofinder.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.model.entities.Classroom

class ClassroomAdapter(
    private val classrooms: List<Classroom>, private val onItemClick: (Classroom) -> Unit
) : RecyclerView.Adapter<ClassroomAdapter.ClassroomViewHolder>() {

    inner class ClassroomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var classroomName: TextView = itemView.findViewById(R.id.classroom_name)
        var classroomCapacity: TextView = itemView.findViewById(R.id.classroom_capacity)
        var classroomType: TextView = itemView.findViewById(R.id.classroom_type)
    }

    override fun getItemCount(): Int {
        return classrooms.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ClassroomViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_classroom, viewGroup, false)
        return ClassroomViewHolder(v)
    }

    override fun onBindViewHolder(classroomViewHolder: ClassroomViewHolder, idx: Int) {
        val classroom = classrooms[idx]

        classroomViewHolder.classroomName.text = classroom.name

        val capacityText = classroomViewHolder.itemView.context.getString(
            R.string.capacity_format, classroom.capacity
        )
        classroomViewHolder.classroomCapacity.text = capacityText

        classroomViewHolder.classroomType.text = classroom.type

        classroomViewHolder.itemView.setOnClickListener {
            onItemClick(classroom)
        }
    }
}