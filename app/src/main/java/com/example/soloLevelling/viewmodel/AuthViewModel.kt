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
    var loggedInUserId = mutableStateOf<Int?>(null)
    var registrationError = mutableStateOf("")

    // Função para verificar o estado de login do usuário
    fun isUserLoggedIn(): Boolean {
        return userRepository.isUserLoggedIn()
    }

    private var registrationSuccess = mutableStateOf(false) // Para sinalizar sucesso no registro

    fun registerUser(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                if (userRepository.isEmailRegistered(email)) {
                    registrationError.value = "O e-mail já está registrado!"
                } else {
                    val user = User(username = username, email = email, password = password)
                    android.util.Log.d("UserRepository", "Usuário registrado com ID: $user")
                    loggedInUserId.value = userRepository.insertUser(user)
                    android.util.Log.d("UserRepository", "ID: ${loggedInUserId.value}")

                    loginSuccess.value = true
                    registrationError.value = ""
                }
            } catch (e: Exception) {
                registrationError.value = "Erro ao registrar o usuário"
            }
        }
    }

    // Após a ação de sucesso, o estado pode ser resetado, se necessário:
    fun resetRegistrationState() {
        registrationSuccess.value = false
    }

    // Função para autenticar usuário
    fun authenticateUser(email: String, password: String) {
        viewModelScope.launch {
            val user = userRepository.authenticateUser(email, password)
            if (user != null) {
                loggedInUserId.value = user.id
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
        loggedInUserId.value = null
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
