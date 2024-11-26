package com.example.soloLevelling.model.repository

import com.example.soloLevelling.model.dao.MissionDao
import com.example.soloLevelling.model.dao.UserMissionDao
import com.example.soloLevelling.model.entity.Mission
import com.example.soloLevelling.model.entity.UserMission

class MissionRepository(private val missionDao: MissionDao, private val userMissionDao: UserMissionDao) {

    suspend fun getAllMissions(): List<Mission> = missionDao.getAllMissions()

    suspend fun getUserMissions(userId: Int): List<UserMission> = userMissionDao.getUserMissions(userId)

    suspend fun insertMissionAndAssignToUser(userId: Int, mission: Mission) {
    println("Inserindo missão...")
    val missionId = missionDao.insertMission(mission)
    println("Missão inserida com ID: $missionId")

    if (missionId <= 0) {
        throw IllegalStateException("Erro: ID inválido para missão após inserção.")
    }

    println("Criando relação para userId: $userId e missionId: $missionId")
    val userMission = UserMission(userId = userId, missionId = missionId.toInt())
    userMissionDao.insertUserMission(userMission)
    println("Relação criada com sucesso.")

}

    suspend fun deleteUserMissionById(missionId: Int) {
        userMissionDao.deleteUserMissionById(missionId)
    }

    suspend fun deleteMission(mission: Mission) {
        missionDao.deleteMission(mission)
    }

    suspend fun updateMission(mission: Mission) {
        missionDao.updateMission(mission)
    }

}
