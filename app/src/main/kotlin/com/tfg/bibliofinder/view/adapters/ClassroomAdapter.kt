package com.tfg.bibliofinder.view.adapters

import android.view.View
import android.widget.TextView
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.model.entities.Classroom

class ClassroomAdapter(
    classrooms: List<Classroom>, onItemClick: (Classroom) -> Unit
) : BaseAdapter<Classroom>(classrooms, onItemClick, null, R.layout.card_classroom) {

    override fun bindItem(view: View, item: Classroom) {
        val classroomName: TextView = view.findViewById(R.id.classroom_name)
        classroomName.text = item.name

        val classroomCapacity: TextView = view.findViewById(R.id.classroom_capacity)
        classroomCapacity.text = view.context.getString(
            R.string.free_spaces_format, item.capacity
        )

        val classroomType: TextView = view.findViewById(R.id.classroom_type)
        classroomType.text = item.type
    }
}