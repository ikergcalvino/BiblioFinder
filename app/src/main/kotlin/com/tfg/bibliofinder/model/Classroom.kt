package com.tfg.bibliofinder.model

data class Classroom(
    val classroomId: Long,
    val name: String,
    val capacity: Int,
    val type: String,
    val libraryId: Long
)

object ClassroomMockDataProvider {

    fun getMockClassrooms(): List<Classroom> {
        val classrooms = mutableListOf<Classroom>()
        classrooms.add(
            Classroom(
                1L, "Sala de Estudio 1", 50, "Área de Estudio en Grupo", 1L
            )
        )
        classrooms.add(
            Classroom(
                2L, "Sala de Estudio 2", 40, "Área de Trabajo en Silencio", 1L
            )
        )
        classrooms.add(
            Classroom(
                3L, "Sala de Reuniones 1", 30, "Sala de Colaboración", 2L
            )
        )
        classrooms.add(
            Classroom(
                4L, "Sala de Reuniones 2", 20, "Sala de Estudio Individual", 2L
            )
        )
        classrooms.add(
            Classroom(
                5L, "Sala de Conferencias", 25, "Sala de Presentaciones", 2L
            )
        )
        return classrooms
    }
}