package com.example.soloLevelling.model.dao

import androidx.room.*
import com.example.soloLevelling.model.entity.Mission

@Dao
interface MissionDao {
    @Query("SELECT * FROM mission")
    suspend fun getAllMissions(): List<Mission> // Adicionado 'suspend'

    @Insert
    suspend fun insertMissions(missions: List<Mission>) // Adicionado 'suspend'
}

