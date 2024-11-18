package com.example.soloLevelling.model.dao

import androidx.room.*
import com.example.soloLevelling.AppDatabase
import com.example.soloLevelling.model.entity.UserMission

@Dao
interface UserMissionDao {
    @Query("SELECT * FROM userMission WHERE userId = :userId")
    suspend fun getUserMissions(userId: Int): List<UserMission>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserMissions(userMissions: List<UserMission>)

    suspend fun assignRandomMissionsToUser(userId: Int, database: AppDatabase) {
        val missionDao = database.missionDao()
        val userMissionDao = database.userMissionDao()

        // Obtém todas as missões disponíveis
        val allMissions = missionDao.getAllMissions()

        // Seleciona 3 missões aleatórias
        val randomMissions = allMissions.shuffled().take(3)

        // Cria relações para o usuário
        val userMissions = randomMissions.map { mission ->
            UserMission(userId = userId, missionId = mission.id)
        }

        // Insere as missões atribuídas ao banco
        userMissionDao.insertUserMissions(userMissions)
    }
}
