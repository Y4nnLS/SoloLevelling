package com.example.soloLevelling.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soloLevelling.AppDatabase
import com.example.soloLevelling.model.repository.UserRepository
import com.example.soloLevelling.viewmodel.AuthViewModel
import com.example.soloLevelling.viewmodel.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val db = AppDatabase.getDatabase(this)
            val userRepository = UserRepository(db.userDao(), getSharedPreferences("UserPrefs", MODE_PRIVATE))
            val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(userRepository))

            AppNavigation(authViewModel)
        }
    }
}

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
            onLoginSuccess = { currentScreen = "HomeScreen" },
            onBackToMenu = { currentScreen = "ChooseScreen" }
        )
        "RegisterScreen" -> RegisterScreen(
            authViewModel = authViewModel,
            onRegisterSuccess = { currentScreen = "HomeScreen" },
            onBackToMenu = { currentScreen = "ChooseScreen" }
        )
        "HomeScreen" -> HomeScreen(
            authViewModel = authViewModel,
            onLogout = { currentScreen = "ChooseScreen" }
        )
    }
}

@Composable
fun ChooseScreen(onLoginClick: () -> Unit, onRegisterClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem-vindo! Escolha uma opção:", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRegisterClick, modifier = Modifier.fillMaxWidth()) {
            Text("Registrar")
        }
    }
}

@Composable
fun HomeScreen(authViewModel: AuthViewModel, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem-vindo à Tela Principal", fontSize = 22.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            authViewModel.logout()
            onLogout() // Retorna à tela de escolha ao fazer logout
        }) {
            Text("Logout")
        }
    }
}
