package com.tfg.bibliofinder.model.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tfg.bibliofinder.model.entities.Booking

@Dao
interface BookingDao {

    // Operaciones de Inserción (Create)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBooking(booking: Booking)

    // Operaciones de Consulta (Read)
    @Query("SELECT * FROM Booking WHERE userId = :userId")
    suspend fun getBookingsForUser(userId: Long): List<Booking>

    // Operaciones de Actualización (Update)
    @Update
    suspend fun updateBooking(booking: Booking)

    // Operaciones de Eliminación (Delete)
    @Delete
    suspend fun deleteBooking(booking: Booking)

    // Otros métodos DAO relacionados con Booking si los necesitas
}