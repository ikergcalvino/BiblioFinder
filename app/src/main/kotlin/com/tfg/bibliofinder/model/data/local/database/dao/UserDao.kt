package com.tfg.bibliofinder.model.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tfg.bibliofinder.model.entities.User

@Dao
interface UserDao {

    // Operaciones de Inserción (Create)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    // Operaciones de Consulta (Read)
    @Query("SELECT * FROM User WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    // Operaciones de Actualización (Update)
    @Update
    suspend fun updateUser(user: User)

    // Operaciones de Eliminación (Delete)
    @Delete
    suspend fun deleteUser(user: User)

    // Otros métodos DAO relacionados con User si los necesitas
}