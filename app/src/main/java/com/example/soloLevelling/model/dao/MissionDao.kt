package com.example.soloLevelling.model.dao

import androidx.room.*
import com.example.soloLevelling.model.entity.Mission

@Dao
interface MissionDao {
    @Query("SELECT * FROM mission")
    suspend fun getAllMissions(): List<Mission> // Adicionado 'suspend'

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMission(mission: Mission) // Para inserir uma única missão

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMissions(missions: List<Mission>) // Para inserir múltiplas missões

    @Delete
    suspend fun deleteMission(mission: Mission)

    @Update
    suspend fun updateMission(mission: Mission)

}

