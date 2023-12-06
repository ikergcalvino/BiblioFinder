package com.tfg.bibliofinder.screens.classroom

import android.view.View
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.CardClassroomBinding
import com.tfg.bibliofinder.entities.Classroom
import com.tfg.bibliofinder.util.BaseAdapter

class ClassroomAdapter(
    classrooms: List<Classroom>, onItemClick: (Classroom) -> Unit
) : BaseAdapter<Classroom>(classrooms, onItemClick, null, R.layout.card_classroom) {

    override fun bindItem(view: View, item: Classroom) {
        val binding = CardClassroomBinding.bind(view)
        val context = view.context

        binding.classroomName.text = item.name
        binding.classroomCapacity.text = context.getString(
            R.string.free_spaces_format, item.capacity
        )
        binding.classroomType.text = item.type
    }
}