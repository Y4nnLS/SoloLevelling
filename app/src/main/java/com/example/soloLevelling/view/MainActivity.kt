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
import com.example.soloLevelling.model.repository.MissionRepository
import com.example.soloLevelling.model.repository.UserRepository
import com.example.soloLevelling.viewmodel.AuthViewModel
import com.example.soloLevelling.viewmodel.AuthViewModelFactory
import com.example.soloLevelling.viewmodel.MissionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val db = AppDatabase.getDatabase(this)
            val userRepository = UserRepository(db.userDao(), getSharedPreferences("UserPrefs", MODE_PRIVATE))
            val missionRepository = MissionRepository(db.missionDao(), db.userMissionDao())

            val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(userRepository))
            val missionViewModel = MissionViewModel(repository = missionRepository)

            AppNavigation(authViewModel = authViewModel, missionViewModel = missionViewModel)
        }
    }
}



@Composable
fun AppNavigation(authViewModel: AuthViewModel, missionViewModel: MissionViewModel) {
    var currentScreen by remember { mutableStateOf("ChooseScreen") }

    when (currentScreen) {
        "ChooseScreen" -> {
            ChooseScreen(
                onLoginClick = {
                    currentScreen = "LoginScreen"
                },
                onRegisterClick = {
                    currentScreen = "RegisterScreen"
                }
            )
        }
        "LoginScreen" -> {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    authViewModel.loggedInUserId.value?.let { userId ->
                        missionViewModel.assignMissionsToUser(userId)
                        currentScreen = "HomeScreen"
                    }
                },
                onBackToMenu = {
                    currentScreen = "ChooseScreen"
                }
            )
        }
        "RegisterScreen" -> {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    authViewModel.loggedInUserId.value?.let { userId ->
                        missionViewModel.assignMissionsToUser(userId)
                        currentScreen = "HomeScreen"
                    }
                },
                onBackToMenu = {
                    currentScreen = "ChooseScreen"
                }
            )
        }
        "HomeScreen" -> {
            val userId = authViewModel.loggedInUserId.value
            if (userId != null) {
                HomeScreen(
                    authViewModel = authViewModel,
                    missionViewModel = missionViewModel,
                    userId = userId,
                    onLogout = {
                        currentScreen = "ChooseScreen"
                    },
                    onNavigateToMissions = {
                        currentScreen = "MissionActivity"
                    }
                )
            }
        }
        "MissionActivity" -> {
            MissionScreen(
                missionViewModel = missionViewModel,
                onBackToHome = {
                    currentScreen = "HomeScreen"
                }
            )
        }

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