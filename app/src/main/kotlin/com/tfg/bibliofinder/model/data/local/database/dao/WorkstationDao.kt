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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkstation(workstation: Workstation)

    @Query("SELECT * FROM Workstation WHERE workstationId = :workstationId")
    suspend fun getWorkstationById(workstationId: Long): Workstation?

    @Query("SELECT * FROM Workstation WHERE classroomId = :classroomId")
    suspend fun getWorkstationsInClassroom(classroomId: Long): List<Workstation>

    @Update
    suspend fun updateWorkstation(workstation: Workstation)

    @Delete
    suspend fun deleteWorkstation(workstation: Workstation)
}