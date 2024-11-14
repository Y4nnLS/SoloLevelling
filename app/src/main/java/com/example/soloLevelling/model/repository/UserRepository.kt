package com.example.soloLevelling.model.repository

import android.content.SharedPreferences
import com.example.soloLevelling.model.entity.User
import com.example.soloLevelling.model.dao.UserDao

class UserRepository(private val userDao: UserDao, private val sharedPreferences: SharedPreferences) {

    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    fun setUserLoggedIn(loggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("isLoggedIn", loggedIn).apply()
    }

    suspend fun insertUser(user: User) {
        userDao.insert(user)
        setUserLoggedIn(true)
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
