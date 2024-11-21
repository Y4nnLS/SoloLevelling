package com.example.soloLevelling.model.repository

import com.example.soloLevelling.model.dao.MissionDao
import com.example.soloLevelling.model.dao.UserMissionDao
import com.example.soloLevelling.model.entity.Mission
import com.example.soloLevelling.model.entity.UserMission

class MissionRepository(private val missionDao: MissionDao, private val userMissionDao: UserMissionDao) {

    suspend fun getAllMissions(): List<Mission> = missionDao.getAllMissions()

    suspend fun getUserMissions(userId: Int): List<UserMission> = userMissionDao.getUserMissions(userId)

    suspend fun insertUserMissions(userMissions: List<UserMission>) {
        userMissionDao.insertUserMissions(userMissions)
    }

    suspend fun assignMissionsToUser(userId: Int) {
        val allMissions = missionDao.getAllMissions()
        val randomMissions = allMissions.shuffled().take(3)

        val userMissions = randomMissions.map { mission ->
            UserMission(userId = userId, missionId = mission.id)
        }

        userMissionDao.insertUserMissions(userMissions)
    }
    suspend fun deleteMission(mission: Mission) {
        missionDao.deleteMission(mission)
    }
    suspend fun insertMission(mission: Mission) {
        missionDao.insertMission(mission)
    }

    suspend fun insertMissions(missions: List<Mission>) {
        missionDao.insertMissions(missions)
    }

    suspend fun updateMission(mission: Mission) {
        missionDao.updateMission(mission)
    }

}
