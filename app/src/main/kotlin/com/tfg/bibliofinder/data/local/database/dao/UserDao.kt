package com.tfg.bibliofinder.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tfg.bibliofinder.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM User WHERE userId = :userId")
    suspend fun getUserById(userId: Long): User?

    @Query("SELECT * FROM User WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT COUNT(*) FROM Workstation WHERE userId = :userId AND state = 'OCCUPIED'")
    suspend fun isUserOccupyingWorkstation(userId: Long): Boolean

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}