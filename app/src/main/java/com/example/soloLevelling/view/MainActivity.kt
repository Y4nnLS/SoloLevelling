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
                    android.util.Log.wtf("Navigation", "Navegando para LoginScreen")
                },
                onRegisterClick = {
                    currentScreen = "RegisterScreen"
                    android.util.Log.wtf("Navigation", "Navegando para RegisterScreen")
                }
            )
        }
        "LoginScreen" -> {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    authViewModel.loggedInUserId.value?.let { userId ->
                        android.util.Log.wtf("Navigation", "Login bem-sucedido, UserID: $userId")
                        missionViewModel.assignMissionsToUser(userId)
                        currentScreen = "HomeScreen"
                    }
                },
                onBackToMenu = {
                    currentScreen = "ChooseScreen"
                    android.util.Log.wtf("Navigation", "Voltando para ChooseScreen")
                }
            )
        }
        "RegisterScreen" -> {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    // Verifica se a função foi chamada
                    android.util.Log.wtf("Navigation", "Entrou no onRegisterSuccess")

                    // Imprime o valor atual de loggedInUserId
                    val userId = authViewModel.loggedInUserId.value
                    if (userId != null) {
                        android.util.Log.wtf("Navigation", "Registro bem-sucedido, UserID: $userId")
                        missionViewModel.assignMissionsToUser(userId) // Atribui missões ao usuário
                        currentScreen = "HomeScreen" // Redireciona para HomeScreen
                    } else {
                        android.util.Log.e("Navigation", "Erro: UserID é nulo após registro bem-sucedido")
                    }
                },
                onBackToMenu = {
                    currentScreen = "ChooseScreen"
                    android.util.Log.wtf("Navigation", "Voltando para ChooseScreen")
                }
            )
        }

        "HomeScreen" -> {
            val userId = authViewModel.loggedInUserId.value
            if (userId != null) {
                android.util.Log.wtf("Navigation", "Carregando HomeScreen para UserID: $userId")
                HomeScreen(
                    authViewModel = authViewModel,
                    missionViewModel = missionViewModel,
                    userId = userId,
                    onLogout = {
                        currentScreen = "ChooseScreen"
                        android.util.Log.wtf("Navigation", "Logout realizado. Voltando para ChooseScreen")
                    }
                )
            } else {
                android.util.Log.e("Navigation", "Erro: UserID é nulo ao tentar acessar HomeScreen")
            }
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