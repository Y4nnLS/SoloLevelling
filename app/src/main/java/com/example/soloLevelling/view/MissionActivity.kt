package com.example.soloLevelling.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soloLevelling.AppDatabase
import com.example.soloLevelling.model.entity.Mission
import com.example.soloLevelling.model.repository.MissionRepository
import com.example.soloLevelling.viewmodel.MissionViewModel

class MissionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val db = AppDatabase.getDatabase(this)
            val missionRepository = MissionRepository(db.missionDao(), db.userMissionDao())
            val missionViewModel = MissionViewModel(repository = missionRepository)

            MissionScreen(
                missionViewModel = missionViewModel,
                onBackToHome = { finish() } // Fecha a MissionActivity e retorna à anterior
            )        }
    }
}
@Composable
fun MissionActivityContent(missionViewModel: MissionViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem-vindo à Gerência de Missões", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // Ação para retornar à HomeScreen, caso necessário
        }) {
            Text("Voltar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionScreen(missionViewModel: MissionViewModel, onBackToHome: () -> Unit) {
    val context = LocalContext.current // Certifique-se de que isso está dentro de um @Composable
    var missionTitle by remember { mutableStateOf("") }
    var missionDescription by remember { mutableStateOf("") }
    var missionPriority by remember { mutableStateOf(1) }
    var missionFrequency by remember { mutableStateOf("DAILY") }
    var editingMission by remember { mutableStateOf<Mission?>(null) } // Estado para edição
    var showDeleteDialog by remember { mutableStateOf(false) } // Estado para mostrar o dialog

    val missions = missionViewModel.missions

    // Listas para os valores de prioridade e frequência
    val priorities = listOf(1, 2, 3) // 1 = Alta, 2 = Média, 3 = Baixa
    val frequencies = listOf("DAILY", "WEEKLY", "MONTHLY")

    // Estados para exibição dos menus suspensos
    var showPriorityMenu by remember { mutableStateOf(false) }
    var showFrequencyMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        // Botão para voltar
        Button(
            onClick = onBackToHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar para Home")
        }


        // Título da tela
        Text("Gerenciamento de Missões", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Lista de missões usando LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(missions) { mission ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Título: ${mission.title}")
                        Text("Descrição: ${mission.description}")
                        Text("Prioridade: ${mission.priority}")
                        Text("Frequência: ${mission.frequency}")
                    }
                    Row {
                        Button(onClick = {
                            editingMission = mission
                            missionTitle = mission.title
                            missionDescription = mission.description
                            missionPriority = mission.priority
                            missionFrequency = mission.frequency
                        }) {
                            Text("Editar")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { showDeleteDialog = true }) {
                            Text("Remover")
                        }

                        // Dialog para confirmar a exclusão
                        if (showDeleteDialog) {
                            AlertDialog(
                                onDismissRequest = { showDeleteDialog = false },
                                title = { Text("Confirmar Exclusão") },
                                text = { Text("Você tem certeza que deseja excluir esta missão?") },
                                confirmButton = {
                                    Button(onClick = {
                                        missionViewModel.deleteMission(mission)
                                        showDeleteDialog = false
                                    }) {
                                        Text("Excluir")
                                    }
                                },
                                dismissButton = {
                                    Button(onClick = { showDeleteDialog = false }) {
                                        Text("Cancelar")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // Formulário para adicionar ou editar missões
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = missionTitle,
                onValueChange = { missionTitle = it },
                label = { Text("Título da Missão") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = missionDescription,
                onValueChange = { missionDescription = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // SelectBox para Prioridade
            Box {
                TextField(
                    value = "Prioridade: $missionPriority",
                    onValueChange = {},
                    label = { Text("Prioridade") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showPriorityMenu = !showPriorityMenu }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Abrir menu de prioridade")
                        }
                    }
                )
                DropdownMenu(
                    expanded = showPriorityMenu,
                    onDismissRequest = { showPriorityMenu = false }
                ) {
                    priorities.forEach { priority ->
                        DropdownMenuItem(
                            text = { Text(priority.toString()) },
                            onClick = {
                                missionPriority = priority
                                showPriorityMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // SelectBox para Frequência
            Box {
                TextField(
                    value = "Frequência: $missionFrequency",
                    onValueChange = {},
                    label = { Text("Frequência") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showFrequencyMenu = !showFrequencyMenu }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Abrir menu de frequência")
                        }
                    }
                )
                DropdownMenu(
                    expanded = showFrequencyMenu,
                    onDismissRequest = { showFrequencyMenu = false }
                ) {
                    frequencies.forEach { frequency ->
                        DropdownMenuItem(
                            text = { Text(frequency) },
                            onClick = {
                                missionFrequency = frequency
                                showFrequencyMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão para adicionar ou confirmar edição
            Button(
                onClick = {
                    if (editingMission != null) {
                        // Confirma edição
                        missionViewModel.updateMission(
                            editingMission!!.copy(
                                title = missionTitle,
                                description = missionDescription,
                                priority = missionPriority,
                                frequency = missionFrequency
                            )
                        )
                        editingMission = null
                    } else {
                        // Adiciona nova missão
                        missionViewModel.addMission(
                            Mission(
                                title = missionTitle,
                                description = missionDescription,
                                priority = missionPriority,
                                frequency = missionFrequency
                            )
                        )
                    }
                    missionTitle = ""
                    missionDescription = ""
                    missionPriority = 1
                    missionFrequency = "DAILY"
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (editingMission != null) "Confirmar Edição" else "Adicionar Missão")
            }
        }
    }
}
