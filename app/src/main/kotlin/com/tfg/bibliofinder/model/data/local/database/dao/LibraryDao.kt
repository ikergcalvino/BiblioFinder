package com.tfg.bibliofinder.model.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tfg.bibliofinder.model.entities.Library

@Dao
interface LibraryDao {

    // Operaciones de Inserción (Create)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLibrary(library: Library)

    // Operaciones de Consulta (Read)
    @Query("SELECT * FROM Library")
    suspend fun getAllLibraries(): List<Library>

    // Operaciones de Actualización (Update)
    @Update
    suspend fun updateLibrary(library: Library)

    // Operaciones de Eliminación (Delete)
    @Delete
    suspend fun deleteLibrary(library: Library)

    // Otros métodos DAO relacionados con Library si los necesitas
}