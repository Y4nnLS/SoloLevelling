package com.example.soloLevelling.model.dao

import androidx.room.*
import com.example.soloLevelling.model.entity.Mission

@Dao
interface MissionDao {
    @Query("SELECT * FROM mission")
    suspend fun getAllMissions(): List<Mission>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMission(mission: Mission): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMissions(missions: List<Mission>)

    @Delete
    suspend fun deleteMission(mission: Mission)

    @Update
    suspend fun updateMission(mission: Mission)

}

