package com.example.soloLevelling.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import com.example.soloLevelling.model.entity.UserMission
import com.example.soloLevelling.viewmodel.MissionViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun MissionManagementScreen(viewModel: MissionViewModel, userId: Int) {
    var userMissions by remember { mutableStateOf<List<UserMission>>(emptyList()) }

    // Fetch user missions inside a coroutine
    LaunchedEffect(userId) {
        userMissions = viewModel.getUserMissions(userId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(
            onClick = { viewModel.assignMissionsToUser(userId) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Atribuir 3 Missões Aleatórias")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Missões do Usuário", fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))

        userMissions.forEach { userMission ->
            Text("Missão ID: ${userMission.missionId}", modifier = Modifier.padding(bottom = 8.dp))
        }
    }
}
