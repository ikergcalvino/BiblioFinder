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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBooking(booking: Booking)

    @Query("SELECT * FROM Booking WHERE bookingId = :bookingId")
    suspend fun getBookingById(bookingId: Long): Booking?

    @Query("SELECT * FROM Booking WHERE userId = :userId")
    suspend fun getBookingsForUser(userId: Long): List<Booking>

    @Update
    suspend fun updateBooking(booking: Booking)

    @Delete
    suspend fun deleteBooking(booking: Booking)
}