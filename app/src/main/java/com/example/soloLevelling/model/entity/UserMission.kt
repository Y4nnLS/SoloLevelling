package com.example.soloLevelling.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userMission")
data class UserMission(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int, // Relaciona a missão ao usuário
    val missionId: Int // Relaciona à missão original
)