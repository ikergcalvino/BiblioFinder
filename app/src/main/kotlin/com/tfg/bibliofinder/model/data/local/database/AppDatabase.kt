package com.tfg.bibliofinder.model.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tfg.bibliofinder.model.data.local.database.dao.BookingDao
import com.tfg.bibliofinder.model.data.local.database.dao.ClassroomDao
import com.tfg.bibliofinder.model.data.local.database.dao.LibraryDao
import com.tfg.bibliofinder.model.data.local.database.dao.UserDao
import com.tfg.bibliofinder.model.data.local.database.dao.WorkstationDao
import com.tfg.bibliofinder.model.entities.Booking
import com.tfg.bibliofinder.model.entities.Classroom
import com.tfg.bibliofinder.model.entities.Library
import com.tfg.bibliofinder.model.entities.User
import com.tfg.bibliofinder.model.entities.Workstation

@Database(
    entities = [Booking::class, Classroom::class, Library::class, User::class, Workstation::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookingDao(): BookingDao
    abstract fun classroomDao(): ClassroomDao
    abstract fun libraryDao(): LibraryDao
    abstract fun userDao(): UserDao
    abstract fun workstationDao(): WorkstationDao

    companion object {
        // Implementación del patrón Singleton para obtener una instancia de la base de datos
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "app_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}