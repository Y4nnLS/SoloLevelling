package com.example.soloLevelling.model.repository

import android.content.SharedPreferences
import com.example.soloLevelling.model.entity.User
import com.example.soloLevelling.model.dao.UserDao

class UserRepository(private val userDao: UserDao, private val sharedPreferences: SharedPreferences) {

    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }
    suspend fun isEmailRegistered(email: String): Boolean {
        return userDao.getUserByEmail(email) != null
    }
    fun setUserLoggedIn(loggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("isLoggedIn", loggedIn).apply()
    }

    suspend fun insertUser(user: User): Int {
        userDao.insert(user)
        setUserLoggedIn(true)
        return user.id
    }

    suspend fun authenticateUser(email: String, password: String): User? {
        val user = userDao.getUserByEmail(email)
        if (user != null && user.password == password) {
            setUserLoggedIn(true)
            return user
        }
        return null
    }
}
