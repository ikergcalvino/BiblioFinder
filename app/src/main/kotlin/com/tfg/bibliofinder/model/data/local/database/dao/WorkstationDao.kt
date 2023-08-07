package com.tfg.bibliofinder.model.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tfg.bibliofinder.model.entities.Workstation

@Dao
interface WorkstationDao {

    // Operaciones de Inserción (Create)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkstation(workstation: Workstation)

    // Operaciones de Consulta (Read)
    @Query("SELECT * FROM Workstation WHERE classroomId = :classroomId")
    suspend fun getWorkstationsInClassroom(classroomId: Long): List<Workstation>

    // Operaciones de Actualización (Update)
    @Update
    suspend fun updateWorkstation(workstation: Workstation)

    // Operaciones de Eliminación (Delete)
    @Delete
    suspend fun deleteWorkstation(workstation: Workstation)

    // Otros métodos DAO relacionados con Workstation si los necesitas
}