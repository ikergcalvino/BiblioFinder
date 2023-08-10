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
    suspend fun getAllLibraries(): List<Library>

    @Query("SELECT * FROM Library")
    fun getAllLibrariesLiveData(): LiveData<List<Library>>

    @Update
    suspend fun updateLibrary(library: Library)

    @Delete
    suspend fun deleteLibrary(library: Library)
}