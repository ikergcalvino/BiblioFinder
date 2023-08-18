package com.tfg.bibliofinder.model.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tfg.bibliofinder.model.entities.Classroom

@Dao
interface ClassroomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassroom(classroom: Classroom)

    @Query("SELECT * FROM Classroom WHERE classroomId = :classroomId")
    suspend fun getClassroomById(classroomId: Long): Classroom?

    @Query("SELECT * FROM Classroom WHERE libraryId = :libraryId")
    fun getClassroomsInLibrary(libraryId: Long): LiveData<List<Classroom>>

    @Update
    suspend fun updateClassroom(classroom: Classroom)

    @Delete
    suspend fun deleteClassroom(classroom: Classroom)
}