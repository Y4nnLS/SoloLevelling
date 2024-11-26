package com.example.soloLevelling.model.dao

import androidx.room.*
import com.example.soloLevelling.AppDatabase
import com.example.soloLevelling.model.entity.Mission
import com.example.soloLevelling.model.entity.UserMission

@Dao
interface UserMissionDao {
    @Query("SELECT * FROM userMission WHERE userId = :userId")
    suspend fun getUserMissions(userId: Int): List<UserMission>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserMission(userMission: UserMission)

    @Query("DELETE FROM userMission WHERE id = :missionId")
    suspend fun deleteUserMissionById(missionId: Int)

}
