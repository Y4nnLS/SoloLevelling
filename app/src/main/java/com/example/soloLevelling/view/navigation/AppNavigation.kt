package com.example.soloLevelling.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.soloLevelling.view.ChooseScreen
import com.example.soloLevelling.view.HomeScreen
import com.example.soloLevelling.view.LoginScreen
import com.example.soloLevelling.view.RegisterScreen
import com.example.soloLevelling.viewmodel.AuthViewModel

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    // Controla a navegação entre as telas
    var currentScreen by remember { mutableStateOf("ChooseScreen") }

    when (currentScreen) {
        "ChooseScreen" -> ChooseScreen(
            onLoginClick = { currentScreen = "LoginScreen" },
            onRegisterClick = { currentScreen = "RegisterScreen" }
        )
        "LoginScreen" -> LoginScreen(
            authViewModel = authViewModel,
            onLoginSuccess = { currentScreen = "HomeScreen" }, // Ao fazer login, vai para HomeScreen
            onBackToMenu = { currentScreen = "ChooseScreen" }   // Ao clicar em "Voltar ao Menu", vai para ChooseScreen
        )
        "RegisterScreen" -> RegisterScreen(
            authViewModel = authViewModel,
            onRegisterSuccess = { currentScreen = "HomeScreen" }, // Ao se registrar, vai para HomeScreen
            onBackToMenu = { currentScreen = "ChooseScreen" }     // Ao clicar em "Voltar ao Menu", vai para ChooseScreen
        )
        "HomeScreen" -> HomeScreen(
            authViewModel = authViewModel,
            onLogout = { currentScreen = "ChooseScreen" }         // Ao fazer logout, volta para ChooseScreen
        )
    }
}
