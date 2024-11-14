package com.example.soloLevelling.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.soloLevelling.model.entity.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?
}
