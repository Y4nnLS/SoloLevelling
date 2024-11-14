package com.example.soloLevelling.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import com.example.soloLevelling.model.entity.User
import com.example.soloLevelling.model.repository.UserRepository

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Estado para sucesso e erro de login
    var loginSuccess = mutableStateOf(false)
    var loginError = mutableStateOf("")

    // Função para verificar o estado de login do usuário
    fun isUserLoggedIn(): Boolean {
        return userRepository.isUserLoggedIn()
    }

    // Função para registrar novo usuário
    fun registerUser(username: String, email: String, password: String) {
        val user = User(username = username, email = email, password = password)
        viewModelScope.launch {
            try {
                userRepository.insertUser(user)
                loginSuccess.value = true
                loginError.value = ""
            } catch (e: Exception) {
                loginError.value = "Erro ao registrar o usuário"
            }
        }
    }

    // Função para autenticar usuário
    fun authenticateUser(email: String, password: String) {
        viewModelScope.launch {
            val user = userRepository.authenticateUser(email, password)
            if (user != null) {
                loginSuccess.value = true
                loginError.value = ""
            } else {
                loginError.value = "Email ou senha incorretos"
            }
        }
    }

    // Função de logout
    fun logout() {
        userRepository.setUserLoggedIn(false)
        loginSuccess.value = false
    }
}

class AuthViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
