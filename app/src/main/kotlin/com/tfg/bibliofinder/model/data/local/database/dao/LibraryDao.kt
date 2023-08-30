package com.tfg.bibliofinder.model.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tfg.bibliofinder.model.entities.Library

@Dao
interface LibraryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLibrary(library: Library)

    @Query("SELECT * FROM Library WHERE libraryId = :libraryId")
    suspend fun getLibraryById(libraryId: Long): Library?

    @Query("SELECT * FROM Library")
    fun getAllLibraries(): LiveData<List<Library>>

    @Query("SELECT COUNT(*) FROM Workstation WHERE classroomId IN (SELECT classroomId FROM Classroom WHERE libraryId = :libraryId) AND state = 'AVAILABLE'")
    suspend fun getNumberOfAvailableWorkstationsInLibrary(libraryId: Long): Int

    @Update
    suspend fun updateLibrary(library: Library)

    @Delete
    suspend fun deleteLibrary(library: Library)
}