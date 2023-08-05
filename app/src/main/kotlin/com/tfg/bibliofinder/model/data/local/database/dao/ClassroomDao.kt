package com.tfg.bibliofinder.model.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tfg.bibliofinder.model.entities.Classroom

@Dao
interface ClassroomDao {

    // Operaciones de Inserción (Create)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClassroom(classroom: Classroom)

    // Operaciones de Consulta (Read)
    @Query("SELECT * FROM Classroom WHERE libraryId = :libraryId")
    suspend fun getClassroomsInLibrary(libraryId: Long): List<Classroom>

    // Operaciones de Actualización (Update)
    @Update
    suspend fun updateClassroom(classroom: Classroom)

    // Operaciones de Eliminación (Delete)
    @Delete
    suspend fun deleteClassroom(classroom: Classroom)

    // Otros métodos DAO relacionados con Classroom si los necesitas
}