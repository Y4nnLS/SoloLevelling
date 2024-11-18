package com.example.soloLevelling.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mission")
data class Mission(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Int, // LOW, MEDIUM, HIGH
    val frequency: String // DAILY, WEEKLY, MONTHLY
)