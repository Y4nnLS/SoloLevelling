//package com.example.soloLevelling.view
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.*
//import androidx.compose.material3.Button
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.*
//import androidx.compose.ui.*
//import androidx.compose.ui.unit.*
//import com.example.soloLevelling.model.entity.Mission
//import com.example.soloLevelling.viewmodel.MissionViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MissionScreen(missionViewModel: MissionViewModel) {
//    var missionTitle by remember { mutableStateOf("") }
//    var missionDescription by remember { mutableStateOf("") }
//    var missionPriority by remember { mutableIntStateOf(0) }
//    var missionFrequency by remember { mutableStateOf("DAILY") }
//
//    val missions = missionViewModel.missions
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.Start
//    ) {
//        Text("Gerenciamento de Missões", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
//
//        LazyColumn(modifier = Modifier.weight(1f)) {
//            items(missions) { mission ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Column {
//                        Text("Título: ${mission.title}")
//                        Text("Descrição: ${mission.description}")
//                        Text("Prioridade: ${mission.priority}")
//                        Text("Frequência: ${mission.frequency}")
//                    }
//                    Row {
//                        Button(onClick = { missionViewModel.deleteMission(mission) }) {
//                            Text("Remover")
//                        }
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Button(onClick = {
//                            missionTitle = mission.title
//                            missionDescription = mission.description
//                            missionPriority = mission.priority
//                            missionFrequency = mission.frequency
//                            missionViewModel.updateMission(
//                                mission.copy(
//                                    title = missionTitle,
//                                    description = missionDescription,
//                                    priority = missionPriority,
//                                    frequency = missionFrequency
//                                )
//                            )
//                        }) {
//                            Text("Editar")
//                        }
//                    }
//                }
//            }
//        }
//
//        TextField(
//            value = missionTitle,
//            onValueChange = { missionTitle = it },
//            label = { Text("Título da Missão") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        TextField(
//            value = missionDescription,
//            onValueChange = { missionDescription = it },
//            label = { Text("Descrição") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        TextField(
//            value = missionPriority.toString(),
//            onValueChange = {
//                missionPriority = it.toIntOrNull() ?: 0 // Converte para Int, padrão 0 se inválido
//            },
//            label = { Text("Prioridade (1, 2, 3)") },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )
//
//        TextField(
//            value = missionFrequency,
//            onValueChange = { missionFrequency = it },
//            label = { Text("Frequência (DAILY, WEEKLY, MONTHLY)") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                missionViewModel.addMission(
//                    Mission(
//                        title = missionTitle,
//                        description = missionDescription,
//                        priority = missionPriority,
//                        frequency = missionFrequency
//                    )
//                )
//                missionTitle = ""
//                missionDescription = ""
//                missionPriority = 1
//                missionFrequency = "DAILY"
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Adicionar Missão")
//        }
//    }
//}
