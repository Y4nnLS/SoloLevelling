package com.example.soloLevelling.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val username: String,
    val email: String,
    val password: String,
)
