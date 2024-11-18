package com.example.soloLevelling.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import com.example.soloLevelling.model.entity.UserMission
import com.example.soloLevelling.viewmodel.AuthViewModel
import com.example.soloLevelling.viewmodel.MissionViewModel

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    missionViewModel: MissionViewModel,
    userId: Int,
    onLogout: () -> Unit
) {
    val userMissions = remember { mutableStateListOf<UserMission>() }

    // Carrega as missões do usuário na inicialização da tela
    LaunchedEffect(userId) {
        val missions = missionViewModel.getUserMissions(userId)
        userMissions.addAll(missions)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Bem-vindo à Tela Principal", fontSize = 22.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Missões Atribuídas", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))

        userMissions.forEach { userMission ->
            val mission = missionViewModel.missions.find { it.id == userMission.missionId }
            if (mission != null) {
                Text("- ${mission.title}: ${mission.description}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            authViewModel.logout()
            onLogout()
        }) {
            Text("Logout")
        }
    }
}
