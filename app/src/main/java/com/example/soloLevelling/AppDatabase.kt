package com.example.soloLevelling

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.soloLevelling.model.dao.MissionDao
import com.example.soloLevelling.model.dao.UserDao
import com.example.soloLevelling.model.dao.UserMissionDao
import com.example.soloLevelling.model.entity.Mission
import com.example.soloLevelling.model.entity.User
import com.example.soloLevelling.model.entity.UserMission

@Database(entities = [User::class, Mission::class, UserMission::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun missionDao(): MissionDao
    abstract fun userMissionDao(): UserMissionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
