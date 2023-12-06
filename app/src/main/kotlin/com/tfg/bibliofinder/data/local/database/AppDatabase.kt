package com.tfg.bibliofinder.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tfg.bibliofinder.data.local.database.dao.ClassroomDao
import com.tfg.bibliofinder.data.local.database.dao.LibraryDao
import com.tfg.bibliofinder.data.local.database.dao.UserDao
import com.tfg.bibliofinder.data.local.database.dao.WorkstationDao
import com.tfg.bibliofinder.entities.Classroom
import com.tfg.bibliofinder.entities.Library
import com.tfg.bibliofinder.entities.User
import com.tfg.bibliofinder.entities.Workstation

@Database(
    entities = [Classroom::class, Library::class, User::class, Workstation::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

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

    suspend fun loadInitialData(
        libraries: List<Library>, classrooms: List<Classroom>, workstations: List<Workstation>
    ) {
        val libraryDao = libraryDao()
        val classroomDao = classroomDao()
        val workstationDao = workstationDao()

        // Insert libraries
        for (library in libraries) {
            libraryDao.insertLibrary(library)
        }

        // Insert classrooms
        for (classroom in classrooms) {
            classroomDao.insertClassroom(classroom)
        }

        // Insert workstations
        for (workstation in workstations) {
            workstationDao.insertWorkstation(workstation)
        }
    }
}