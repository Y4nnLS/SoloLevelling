package com.example.soloLevelling.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soloLevelling.AppDatabase
import com.example.soloLevelling.model.entity.Mission

@Composable
fun UserMissionsScreen(userId: Int, database: AppDatabase) {
    val userMissionDao = database.userMissionDao()
    val missionDao = database.missionDao()

    // Estados para armazenar missões
    var missions by remember { mutableStateOf<List<Mission>>(emptyList()) }

    // Executa a lógica de carregamento em uma coroutine
    LaunchedEffect(userId) {
        val userMissions = userMissionDao.getUserMissions(userId)
        missions = userMissions.mapNotNull { userMission ->
            missionDao.getAllMissions().find { it.id == userMission.missionId }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Your Missions", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        missions.forEach { mission ->
            Text(
                text = "${mission.title}: ${mission.description} (Priority: ${mission.priority}, Frequency: ${mission.frequency})",
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}
