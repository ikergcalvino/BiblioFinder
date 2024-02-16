package com.tfg.bibliofinder.screens.classroom

import android.view.View
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.CardClassroomBinding
import com.tfg.bibliofinder.entities.Classroom
import com.tfg.bibliofinder.util.BaseAdapter
import com.tfg.bibliofinder.util.ItemClickListener

class ClassroomAdapter(
    classrooms: List<Classroom>, clickListener: ItemClickListener<Classroom>
) : BaseAdapter<Classroom>(classrooms, clickListener, R.layout.card_classroom) {

    override fun bindItem(view: View, item: Classroom) {
        val binding = CardClassroomBinding.bind(view)

        binding.apply {
            classroomName.text = item.name
            classroomCapacity.text =
                view.context.getString(R.string.free_spaces_format, item.freeSpaces, item.capacity)
            classroomType.text = item.type
        }
    }
}